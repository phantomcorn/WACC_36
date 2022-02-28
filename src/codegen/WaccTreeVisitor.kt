package codegen

import codegen.instr.*
import codegen.instr.loadable.*
import codegen.instr.operand2.*
import codegen.instr.register.*
import parse.expr.*
import parse.stat.*
import kotlin.collections.ArrayDeque
import codegen.utils.RegisterIterator
import codegen.utils.StringTable
import codegen.utils.VariablePointer
import parse.semantics.SymbolTable
import parse.symbols.Array
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int
import parse.symbols.String
import parse.symbols.Type
import parse.symbols.PairInstance
import parse.func.FuncAST
import parse.func.ParamList
import parse.func.Parameter

class WaccTreeVisitor(st: SymbolTable<Type>) : ASTVisitor {
    val regsInUse = ArrayDeque<MutableSet<Register>>()
    val availableRegisters = RegisterIterator()
    var symbolTable = st
    var variableST = SymbolTable<Pair<kotlin.Int, kotlin.Int>>(null)
    var offsetStack = ArrayDeque<kotlin.Int>()

    companion object {
        val funcTable = SymbolTable<FuncObj>(null)
        val stringTable = StringTable()
    }

    fun stackPush(st: SymbolTable<Type>) {
        regsInUse.addFirst(regsInUse.first())
        symbolTable = st
        variableST = SymbolTable(variableST)
        VariablePointer.push()
        offsetStack.addFirst(calcStackAlloc(symbolTable))
    }

    fun stackPop() {
        availableRegisters.add(regsInUse.removeFirst() subtract regsInUse.first())
        symbolTable = symbolTable.getTable()!!
        variableST = variableST.getTable()!!
        VariablePointer.pop()
        offsetStack.removeFirst()
    }

    fun calcStackAlloc(st: SymbolTable<Type>): kotlin.Int {
        var size = 0
        for (k in st.dict.keys) {
            size += symbolTable.lookup(k)!!.getByteSize()
        }
        return size
    }

    fun calcVarOffset(name: kotlin.String): ImmediateOffset {
        val (initialOffset, level) = variableST.lookupAll(name)!!
        var offset: kotlin.Int = initialOffset

        for (i in (level + 1)..VariablePointer.level()) {
            offset += offsetStack.get(i)
        }
        return ImmediateOffset(SP, offset)
    }

    /* Begin at root of AST. */
    override fun visitAST(root : ASTNode): List<Instruction> {
        regsInUse.addFirst(mutableSetOf<Register>())
        return root.accept(this)
    }

    override fun visitFunction(node: FuncAST) {
        var offset = 0
        for (i in (node.params.values.size - 1)..0) {
            offset += node.params.values[i].paramType!!.getByteSize()
            variableST.add(node.params.values[i].paramName, ImmediateOffset(SP(0), offset))
        }
        val funcObj = funcTable.lookup(node.id)!!
        funcObj.funcBody.addAll(visitAST(node.body))
    }

    /* Code generation for statements. */

    override fun visitSkipNode(): List<Instruction> {
        return listOf<Instruction>()
    }

    override fun visitWhileNode(node: While): List<Instruction> {
        val bodyLabel = Label()
        val condLabel = Label()

        val result = mutableListOf<Instruction>(Branch(condLabel.name), bodyLabel)
        stackPush(node.st)
        result.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        result.addAll(node.s.accept(this))
        stackPop()
        result.add(condLabel)
        val rd = availableRegisters.peek()
        result.addAll(node.e.accept(this))
        result.add(Compare(rd, Immediate(1)))
        regsInUse.first().remove(rd)
        result.add(Branch(bodyLabel.name, Cond.EQ))

        return result
    }

    override fun visitDeclarationNode(node: Declaration): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()

        VariablePointer.decrement(node.t.getByteSize())
        variableST.add(node.id, kotlin.Pair(VariablePointer.getCurrentOffset(), VariablePointer.level()))

        result.addAll(node.rhs.accept(this))
        return result
    }

    override fun visitAssignmentNode(node: Assignment): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.rhs.accept(this))
        val (lhsInstrs, lhsLoadable) = node.lhs.acceptLhs(this)
        result.addAll(lhsInstrs)
        result.add(Store(rd, lhsLoadable))
        return result
    }

    override fun visitReadNode(node: Read): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitExitNode(node: Exit): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintNode(node: Print): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintlnNode(node: Println): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStatListNode(statList: StatList): List<Instruction> {
        return statList.list.flatMap { x -> x.accept(this) }
    }

    override fun visitFreeNode(node: Free): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(node: If): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val endLabel = Label()
        val elseLabel = Label()

        val rd = availableRegisters.peek()
        result.addAll(node.e.accept(this))
        result.add(Compare(rd, Immediate(0)))
        result.add(Branch(elseLabel.name, Cond.EQ))

        // start VariablePointer at 0 to determined how many byte to allocate for reg SP

        stackPush(node.st1)
        result.add(Subtract(SP, SP, Immediate(calcStackAlloc(node.st1))))
        result.addAll(node.s1.accept(this))
        stackPop()

        result.add(Branch(endLabel.name))
        result.add(elseLabel)

        stackPush(node.st2)
        result.add(Subtract(SP, SP, Immediate(calcStackAlloc(node.st2))))
        result.addAll(node.s2.accept(this))
        stackPop()

        result.add(endLabel)
        return result
    }

    override fun visitBeginNode(node: Begin): List<Instruction> {
        val result = mutableListOf<Instruction>()

        stackPush(node.st)
        result.add(Subtract(SP, SP, Immediate(calcStackAlloc(node.st))))
        result.addAll(node.s.accept(this))
        stackPop()

        return result
    }

    override fun visitReturnNode(node: Return): List<Instruction> {
        TODO("Not Implemented")
    }

    override fun visitVariableNode(node: Variable): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(Load(rd, calcVarOffset(node.text)))
    }

    override fun visitNewPairNode(node: NewPair): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCallNode(node: Call): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val rd = availableRegisters.peek()
        for (e in node.values) {
            result.addAll(e.accept(this))
            result.add(Store(rd, PreImmediateOffset(SP(0), -e.type!!.getByteSize())))
            availableRegisters.add(rd)
        }
        result.add(BranchWithLink(funcTable.lookup(node.id)!!.funcName))
        return result
    }

    override fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral): List<Instruction> {
        TODO("Not yet implemented")
    }

    /*
    override fun visitArrayInstanceNode(node: ArrayInstance): List<Instruction> {
        TODO("Not yet implemented")
    }
    */

    /* Code generation for types. */

    override fun visitIntNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitTypelessPairNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairInstanceNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArgListNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairElemNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayElemNode(node: ArrayElem): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for binary operators. */

    override fun visitBinaryOp(node : BinaryOp): List<Instruction> {

        val instructions = mutableListOf<Instruction>()

        val lhs : List<Instruction>
        val rhs : List<Instruction>

        val rd = availableRegisters.next()
        val rn = availableRegisters.next()


        if (node.e1.weight > node.e2.weight) {
            availableRegisters.add(rn)
            availableRegisters.add(rd)
            lhs = node.e1.accept(this) //regInUse stores rd
            rhs = node.e2.accept(this) //regInUse stores rn
        } else {
            availableRegisters.add(rd)
            availableRegisters.add(rn)
            rhs = node.e2.accept(this) //regInUse stores rn
            lhs = node.e1.accept(this) //regInUse stores rd
        }


        val binOpInstr : Instruction = when (node.binOp) {
            BinaryOperator.AND ->
                And(rd, rd, rn)
            BinaryOperator.OR ->
                Or(rd, rd, rn)
            BinaryOperator.MULTI ->
                Multiply(rd, rd, rn)
            BinaryOperator.DIV ->
                Div(rd, rd, rn)
            BinaryOperator.MOD ->
                Mod(rd, rd, rn)
            BinaryOperator.PLUS ->
                Add(rd, rd, rn)
            BinaryOperator.MINUS ->
                Subtract(rd, rd, rn)
            BinaryOperator.GT, BinaryOperator.GTE, BinaryOperator.LT, BinaryOperator.LTE, BinaryOperator.EQUIV, BinaryOperator.NOTEQUIV ->
                Compare(rd, rn)


        }

        regsInUse.first().remove(rn) //remove rn

        instructions.addAll(lhs)
        instructions.addAll(rhs)
        instructions.add(binOpInstr)

        return instructions
    }

    /* Code generation for literals. */

    override fun visitIntLiteralNode(node: IntLiteral): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(Load(rd, Immediate(node.value!!)))
    }

    override fun visitCharLiteralNode(node: CharLiteral): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(Move(rd, ImmediateChar(node.value!!)))
    }

    override fun visitBooleanLiteralNode(node: BooleanLiteral): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        val v = if (node.value!!) 1 else 0
        return listOf<Instruction>(Move(rd, Immediate(v)))
    }

    override fun visitStringLiteralNode(node: StringLiteral): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(Load(rd, stringTable.add(node.value!!)))
    }

    override fun visitPairLiteralNode(node: PairLiteral): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiteralNode(node: ArrayLiteral): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for unary operators. */

    override fun visitUnaryOpNode(node: UnaryOp): List<Instruction> {

        val instructions : MutableList<Instruction> = emptyList<Instruction>() as MutableList<Instruction>

        val rd = availableRegisters.peek()
        val exprInstr : List<Instruction> = node.e.accept(this)

        val unOpInstr : Instruction = when (node.op) {
            UnaryOperator.CHR -> {
                Move(rd, ImmediateChar(rd.value.toChar()))
            }
            UnaryOperator.LEN -> {
                Load(rd, Immediate((node.e as StringLiteral).value!!.length))
            }
            UnaryOperator.ORD -> {
                Move(rd, ImmediateChar((node.e as CharLiteral).value!!))
            }
            UnaryOperator.NEG -> {
                ReverseSubtract(rd, rd, Immediate(0))
            }
            UnaryOperator.NOT -> {
                Compare(rd, Immediate(0))
            }
        }



        instructions.addAll(exprInstr)
        instructions.add(unOpInstr)

        return instructions
    }

    override fun visitPairElemNode(node: PairElem): List<Instruction> {
        TODO("Not Implemented")
    }

    override fun visitPairElemLhs(node: PairElem): Pair<List<Instruction>, Loadable> {
        val rd = availableRegisters.peek()
        val instrs = node.e.accept(this)
        if (node.text == "fst") {
            return Pair(instrs, ZeroOffset(rd))
        } else {
            return Pair(instrs, ImmediateOffset(rd, (node.e.type as PairInstance).t1!!.getByteSize()))
        }
    }

    override fun visitVariableLhs(node: Variable): Pair<List<Instruction>, Loadable> {
        return Pair(listOf<Instruction>(), calcVarOffset(node.text))
    }

    override fun visitArrayElemLhs(node: ArrayElem): Pair<List<Instruction>, Loadable> {
        val instrs = mutableListOf<Instruction>()

        val base = availableRegisters.next()
        regsInUse.first().add(base)
        instrs.add(Load(base, calcVarOffset(node.id)))

        val typeSize = availableRegisters.next()
        regsInUse.first().add(typeSize)
        //Load pointer size into typeSize
        instrs.add(Load(typeSize, Immediate(4)))

        val offset = availableRegisters.peek()
        for (i in 0..(node.dims - 2)) {
            //Evaluate expr i and store in offset
            instrs.addAll(node.values[i].accept(this))
            //Multiply offset by the size of a pointer
            instrs.add(Multiply(offset, offset, typeSize))
            //Shift by 4 to adjust for the length parameter at the start of the array
            instrs.add(Add(offset, offset, Immediate(Int.getByteSize())))
            //Load array_i[offset] into base
            instrs.add(Load(base, RegisterOffset(base, offset)))
            regsInUse.first().remove(offset)
            availableRegisters.add(offset)
        }

        //Final expr result stored in offset
        instrs.addAll(node.values[node.dims - 1].accept(this))

        //Load type size in bytes into typeSize
        instrs.add(Load(typeSize, Immediate(node.type!!.getBaseType()!!.getByteSize())))

        //Multiply offset by the typeSize in bytes
        instrs.add(Multiply(offset, offset, typeSize))
        //Shift by 4 to adjust for the length parameter at the start of the array
        instrs.add(Add(offset, offset, Immediate(Int.getByteSize())))
        //Load array_i[offset] into base
        instrs.add(Load(base, RegisterOffset(base, offset)))

        regsInUse.first().remove(typeSize)
        availableRegisters.add(typeSize)
        regsInUse.first().remove(offset)
        availableRegisters.add(offset)

        return Pair(instrs, ZeroOffset(base))
    }
}
