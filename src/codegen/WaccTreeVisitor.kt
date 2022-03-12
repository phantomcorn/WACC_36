package codegen

import codegen.instr.*
import codegen.instr.loadable.*
import codegen.instr.operand2.*
import codegen.instr.register.*
import parse.expr.*
import parse.stat.*
import kotlin.collections.ArrayDeque
import parse.semantics.SymbolTable
import parse.symbols.Int
import parse.symbols.Type
import parse.symbols.PairInstance
import parse.func.FuncAST
import codegen.instr.And
import codegen.instr.Or
import codegen.instr.Div
import codegen.instr.Mod
import codegen.instr.operand2.Immediate.Companion.RETURN_ADDRESS_SIZE
import codegen.instr.operand2.Immediate.Companion.MAX_VALUE
import codegen.utils.*
import parse.symbols.Boolean.True

class WaccTreeVisitor(st: SymbolTable<Type>) : ASTVisitor {
    val regsInUse = ArrayDeque<MutableSet<Register>>()
    val availableRegisters = RegisterIterator()
    var symbolTable = st
    var variableST = SymbolTable<Pair<kotlin.Int, kotlin.Int>>(null)
    var offsetStack = ArrayDeque<kotlin.Int>()
    var preImmOffset = 0

    companion object {
        val funcTable = SymbolTable<FuncObj>(null)
        val stringTable = StringTable()
    }

    fun stackPush(st: SymbolTable<Type>): List<Instruction> {
        regsInUse.addFirst(regsInUse.first())
        symbolTable = st
        variableST = SymbolTable(variableST)
        VariablePointer.push()
        offsetStack.addFirst(calcStackAlloc(symbolTable))
        val instructions = mutableListOf<Instruction>()
        if (offsetStack.first() != 0) {
            var total = offsetStack.first()
            while (total >= MAX_VALUE) {
                instructions.add(Subtract(SP, SP, Immediate(MAX_VALUE)))
                total -= MAX_VALUE
            }
            instructions.add(Subtract(SP, SP, Immediate(total)))
        }
        return instructions
    }

    fun stackPop(): List<Instruction> {
        availableRegisters.add(regsInUse.removeFirst() subtract regsInUse.first())
        symbolTable = symbolTable.getTable()!!
        variableST = variableST.getTable()!!
        VariablePointer.pop()
        val instructions = mutableListOf<Instruction>()
        if (offsetStack.first() != 0) {
            var total = offsetStack.first()
            while (total >= MAX_VALUE) {
                instructions.add(Add(SP, SP, Immediate(MAX_VALUE)))
                total -= MAX_VALUE
            }
            instructions.add(Add(SP, SP, Immediate(total)))
        }
        offsetStack.removeFirst()
        return instructions
    }

    fun calcStackAlloc(st: SymbolTable<Type>): kotlin.Int {
        var size = 0
        for (k in st.dict.keys) {
            size += symbolTable.lookup(k)!!.getByteSize()
        }
        return size
    }

    fun calcVarOffset(name: String): Loadable {
        val (initialOffset, level) = variableST.lookupAll(name)!!
        var offset: kotlin.Int = initialOffset + preImmOffset
        for (i in level..VariablePointer.level()) {
            offset += offsetStack.get(offsetStack.size - 1 - i)
        }
        if (offset == 0) {
            return ZeroOffset(SP)
        } else {
            return ImmediateOffset(SP, Immediate(offset))
        }
    }

    fun store(r: Register, l: Loadable, s: kotlin.Int, c: Cond = Cond(Condition.AL)): Instruction {
        if (s == 1) {
            return StoreByte(r, l, c)
        } else {
            return Store(r, l, c)
        }
    }

    fun load(r: Register, l: Loadable, s: kotlin.Int, c: Cond = Cond(Condition.AL)): Instruction {
        if (s == 1) {
            return LoadByte(r, l, c)
        } else {
            return Load(r, l, c)
        }
    }

    fun readLabel(type : Type): Pair<String,Msg> {
        when (type) {
            is parse.symbols.Int -> {
                return Pair("p_read_int", stringTable.add("%d\\0"))
            }
            else -> {
                return Pair("p_read_char", stringTable.add(" %c\\0"))
            }
        }
    }

    fun printLabel(type : Type): String {
        when (type) {
            is parse.symbols.String -> {
                PrintFuncs.printString()
                return "p_print_string"
            }
            is parse.symbols.Int -> {
                PrintFuncs.printInt()
                return "p_print_int"

            }
            is parse.symbols.Char -> {
                return "putchar"
            }
            is parse.symbols.Boolean -> {
                PrintFuncs.printBoolean()
                return "p_print_bool"
            }
            is parse.symbols.Array -> {
                if (type.getBaseType()!! is parse.symbols.Char) {
                    PrintFuncs.printString()
                    return "p_print_string"
                } else {
                    PrintFuncs.printReference()
                    return "p_print_reference"
                }
            }
            else -> {
                PrintFuncs.printReference()
                return "p_print_reference"
            }
        }
    }

    /* Begin at root of AST. */
    override fun visitAST(root: ASTNode): List<Instruction> {
        regsInUse.addFirst(mutableSetOf<Register>())
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(stackPush(symbolTable))
        instructions.addAll(root.accept(this))
        if (offsetStack.first() != 0) {
            println("test")
            var total = calcStackAlloc(symbolTable)
            while (total >= MAX_VALUE) {
                instructions.add(Add(SP, SP, Immediate(MAX_VALUE)))
                total -= MAX_VALUE
            }
            instructions.add(Add(SP, SP, Immediate(total)))
        }
        VariablePointer.pop()
        return instructions
    }

    override fun visitFunction(node: FuncAST) {
        var offset = 0
        for (i in 0..(node.params.values.size - 1)) {
	    //+4 to take return address on SP into account
	    variableST.add(node.params.values[i].paramName, Pair(RETURN_ADDRESS_SIZE + offset,0))
            offset += node.params.values[i].paramType!!.getByteSize()
        }
        val funcObj = funcTable.lookup(node.id)!!
        regsInUse.addFirst(mutableSetOf<Register>())
        val instructions = mutableListOf<Instruction>()

        regsInUse.addFirst(regsInUse.first())
        symbolTable = node.st
        variableST = SymbolTable(variableST)
        VariablePointer.push()
        offsetStack.addFirst(calcStackAlloc(symbolTable) - offset - node.st.lookup("$")!!.getByteSize())

        if (offsetStack.first() != 0) {
            var total = offsetStack.first()
            while (total >= MAX_VALUE) {
                instructions.add(Subtract(SP, SP, Immediate(MAX_VALUE)))
                total -= MAX_VALUE
            }
            instructions.add(Subtract(SP, SP, Immediate(total)))
            instructions.addAll(node.body.accept(this))
            total = offsetStack.first()
            while (total >= MAX_VALUE) {
                instructions.add(Add(SP, SP, Immediate(MAX_VALUE)))
                total -= MAX_VALUE
            }
            instructions.add(Add(SP, SP, Immediate(total)))
	        instructions.add(Pop(listOf<Register>(PC)))
        } else {
            instructions.addAll(node.body.accept(this))
            offsetStack.removeFirst()
        }
        VariablePointer.pop()

        funcObj.funcBody.addAll(instructions)
        funcObj.user = true
    }

    /* Code generation for statements. */

    override fun visitSkipNode(): List<Instruction> {
        return listOf<Instruction>()
    }

    override fun visitWhileNode(node: While): List<Instruction> {
        val bodyLabel = Label()
        val condLabel = Label()

        val instructions = mutableListOf<Instruction>(Branch(condLabel.name), bodyLabel)
        instructions.addAll(stackPush(node.st))
        instructions.addAll(node.s.accept(this))
        instructions.addAll(stackPop())
        instructions.add(condLabel)
        val rd = availableRegisters.peek()
        instructions.addAll(node.e.accept(this))
        instructions.add(Compare(rd, Immediate(1)))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        instructions.add(Branch(bodyLabel.name, Cond(Condition.EQ)))

        return instructions
    }

    override fun visitDeclarationNode(node: Declaration): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()

        VariablePointer.decrement(node.t.getByteSize())
        variableST.add(node.id, kotlin.Pair(VariablePointer.getCurrentOffset(), VariablePointer.level()))

        instructions.addAll(node.rhs.accept(this))

        //rd holds rhs value
        val index = calcVarOffset(node.id)
        instructions.add(store(rd, index, node.t.getByteSize()))

        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return instructions
    }

    override fun visitAssignmentNode(node: Assignment): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(node.rhs.accept(this))

        val rn = availableRegisters.peek()
        val (childInstrs, loadable) = node.lhs.acceptLhs(this)
        instructions.addAll(childInstrs)
        instructions.add(store(rd, loadable, node.rhs.type()!!.getByteSize()))

        if (rn in regsInUse.first()) {
            availableRegisters.add(rn)
            regsInUse.first().remove(rn)
        }
        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return instructions
    }

    override fun visitReadNode(node: Read): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val (childInstrs, dest) = node.lhs.acceptLhs(this)
        instructions.addAll(childInstrs)
        var offset = Immediate(0) 
        val reg: Register
        if (dest is ZeroOffset) {
            reg = dest.r
        } else {
            offset = (dest as ImmediateOffset).value
            reg = dest.r
        }
        instructions.add(Add(RegisterIterator.r0, reg, offset))
    
        val (label, msg) = readLabel(node.lhs.type()!!)
        instructions.add(BranchWithLink(label))

        if (funcTable.lookupAll(label) == null) {
            val readFunc = FuncObj("")
            readFunc.funcName = label
            val readInstr = mutableListOf<Instruction>()

            readInstr.add(Move(RegisterIterator.r1, RegisterIterator.r0))

            //add msg_n to R0
            readInstr.add(load(RegisterIterator.r0, msg, Int.getByteSize()))

            readInstr.add(Add(RegisterIterator.r0, RegisterIterator.r0, Immediate(4)))
            readInstr.add(BranchWithLink("scanf"))

            readFunc.funcBody = readInstr
            readFunc.funcName = label
            funcTable.add(label, readFunc)
        }
        return instructions
    }

    override fun visitExitNode(node: Exit): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val rd = availableRegisters.peek()
        val exprInstr = node.e.accept(this)

        instructions.addAll(exprInstr)
        instructions.add(Move(RegisterIterator.r0, rd))
        instructions.add(BranchWithLink("exit"))

        availableRegisters.add(rd)
        regsInUse.first().add(rd)

        return instructions
    }

    override fun visitPrintNode(node: Print): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(node.e.accept(this))
        instructions.add(Move(RegisterIterator.r0, rd))

        val label = printLabel(node.e.type!!)
        instructions.add(BranchWithLink(label))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        return instructions
    }

    override fun visitPrintlnNode(node: Println): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(node.e.accept(this))
        instructions.add(Move(RegisterIterator.r0, rd))

        val label = printLabel(node.e.type!!)
        instructions.add(BranchWithLink(label))
        instructions.add(BranchWithLink("p_print_ln"))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        PrintFuncs.printLn()
        return instructions
    }

    override fun visitStatListNode(statList: StatList): List<Instruction> {
        return statList.list.flatMap { x -> x.accept(this) }
    }

    override fun visitFreeNode(node: Free): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(node.e.accept(this))
        instructions.add(Move(RegisterIterator.r0, rd))
        when(node.e.type()!!) {
            is parse.symbols.Array -> {
                instructions.add(BranchWithLink("p_free_array"))
                FreeFuncs.freeArray()
            }
            is parse.symbols.Pair -> {
                instructions.add(BranchWithLink("p_free_pair"))
                FreeFuncs.freePair()
            }
            else -> {
                throw Exception("Not Reached")
            }
        }
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        return instructions
    }

    override fun visitIfNode(node: If): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        val endLabel = Label()
        val elseLabel = Label()

        val rd = availableRegisters.peek()
        instructions.addAll(node.e.accept(this))
        instructions.add(Compare(rd, Immediate(0)))
        instructions.add(Branch(elseLabel.name, Cond(Condition.EQ)))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)

        // start VariablePointer at 0 to determined how many byte to allocate for reg SP

        instructions.addAll(stackPush(node.st1))
        instructions.addAll(node.s1.accept(this))
        instructions.addAll(stackPop())
        instructions.add(Branch(endLabel.name))
        instructions.add(elseLabel)

        instructions.addAll(stackPush(node.st2))
        instructions.addAll(node.s2.accept(this))
        instructions.addAll(stackPop())

        instructions.add(endLabel)
        return instructions
    }

    override fun visitBeginNode(node: Begin): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        instructions.addAll(stackPush(node.st))
        instructions.addAll(node.s.accept(this))
        instructions.addAll(stackPop())

        return instructions
    }

    override fun visitReturnNode(node: Return): List<Instruction> {
        val rd = availableRegisters.peek()
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(node.e.accept(this))
        instructions.add(Move(RegisterIterator.r0, rd))

        var sum = 0
        for (i in offsetStack) {
            sum += i
        }
        if (sum != 0) {
            instructions.add(Add(SP, SP, Immediate(sum)))
        }

        instructions.add(Pop(listOf<Register>(PC)))
        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return instructions
    }

    override fun visitVariableNode(node: Variable): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(load(rd, calcVarOffset(node.text), node.type!!.getByteSize()))
    }

    override fun visitNewPairNode(node: NewPair): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        val rn = availableRegisters.peek()
        instructions.add(Load(RegisterIterator.r0, Immediate(8)))
        instructions.add(BranchWithLink("malloc"))
        instructions.add(Move(rd, RegisterIterator.r0))
        instructions.addAll(node.e1.accept(this))
        instructions.add(Load(RegisterIterator.r0, Immediate(node.e1.type!!.getByteSize())))
        instructions.add(BranchWithLink("malloc"))
        instructions.add(store(rn, ZeroOffset(RegisterIterator.r0), node.e1.type.getByteSize()))
        instructions.add(Store(RegisterIterator.r0, ZeroOffset(rd)))
        regsInUse.first().remove(rn)
        availableRegisters.add(rn)
        instructions.addAll(node.e2.accept(this))
        instructions.add(Load(RegisterIterator.r0, Immediate(node.e2.type!!.getByteSize())))
        instructions.add(BranchWithLink("malloc"))
        instructions.add(store(rn, ZeroOffset(RegisterIterator.r0), node.e2.type.getByteSize()))
        instructions.add(Store(RegisterIterator.r0, ImmediateOffset(rd, Immediate(4))))
        regsInUse.first().remove(rn)
        availableRegisters.add(rn)
        return instructions
    }

    override fun visitCallNode(node: Call): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        val rd = availableRegisters.peek()
        var totalSize = 0
        val regsSaved = regsInUse.first().toList()
        SaveRegisters.saveRegisters(regsSaved)
        for (e in node.values.reversed()) {
            val sizeType = e.type!!.getByteSize()
            instructions.addAll(e.accept(this))
            instructions.add(store(rd, PreImmediateOffset(SP, Immediate(-e.type()!!.getByteSize())), e.type.getByteSize()))
            preImmOffset += sizeType
            availableRegisters.add(rd)
            totalSize += sizeType
        }
        instructions.add(BranchWithLink(funcTable.lookup(node.id)!!.funcName))
        instructions.add(Add(SP, SP, Immediate(totalSize)))
        instructions.add(Move(rd, RegisterIterator.r0))
        availableRegisters.next()
        regsInUse.first().add(rd)
        SaveRegisters.restoreRegisters(regsSaved)
        preImmOffset = 0
        return instructions
    }

    override fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        instructions.add(Load(RegisterIterator.r0, Immediate(4)))
        instructions.add(BranchWithLink("malloc"))
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        instructions.add(Move(rd, RegisterIterator.r0))
        val rn = availableRegisters.next()
        regsInUse.first().add(rn)
        instructions.add(Load(rn, Immediate(0)))
        instructions.add(Store(rn, ZeroOffset(rd)))
        availableRegisters.add(rn)
        regsInUse.first().remove(rn)
        return instructions
    }

    /* Code generation for types. */

    override fun visitIntNode(): List<Instruction> {
        return listOf()
    }

    override fun visitBooleanNode(): List<Instruction> {
        return listOf()
    }

    override fun visitCharNode(): List<Instruction> {
        return listOf()
    }

    override fun visitStringNode(): List<Instruction> {
        return listOf()
    }

    override fun visitTypelessPairNode(): List<Instruction> {
        return listOf()
    }

    override fun visitPairInstanceNode(): List<Instruction> {
        return listOf()
    }

    override fun visitArgListNode(): List<Instruction> {
        return listOf()
    }


    override fun visitArrayElemNode(node: ArrayElem): List<Instruction> {
        val rd = availableRegisters.peek()
        val (childInstrs, loadable) = visitArrayElemLhs(node)
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(childInstrs)
        instructions.add(load(rd, loadable, node.type()!!.getBaseType()!!.getByteSize()))
        return instructions
    }

    /* Code generation for binary operators. */

    override fun visitBinaryOp(node : BinaryOp): List<Instruction> {

        val instructions = mutableListOf<Instruction>()

        val rd = availableRegisters.next()
        val rn = availableRegisters.next()

        if (node.e1.weight >= node.e2.weight) {
            availableRegisters.add(rn)
            availableRegisters.add(rd)
            instructions.addAll(node.e1.accept(this)) //regInUse stores rd
            instructions.addAll(node.e2.accept(this)) //regInUse stores rn
        } else {
            availableRegisters.add(rd)
            availableRegisters.add(rn)
            instructions.addAll(node.e2.accept(this)) //regInUse stores rn
            instructions.addAll(node.e1.accept(this)) //regInUse stores rd
        }

        when (node.binOp) {
            BinaryOperator.AND ->
                instructions.add(And(rd, rd, rn))
            BinaryOperator.OR ->
                instructions.add(Or(rd, rd, rn))
            BinaryOperator.MULTI -> {
                //Needs to be CMP r5 r5 ASR #31 but addressing modes not done yet
                ErrorFuncs.visitOverflowError()
                instructions.add(Multiply(rd, rn, rd, rn))
                instructions.add(Compare(rn, ShiftOffset(rd, Immediate(31), Shift.ASR)))
                instructions.add(BranchWithLink("p_throw_overflow_error", Cond(Condition.NE)))
            }
            BinaryOperator.DIV -> {
                ErrorFuncs.visitDivideByZeroError()
                instructions.add(Move(RegisterIterator.r0, rd))
                instructions.add(Move(RegisterIterator.r1, rn))
                instructions.add(BranchWithLink("p_check_divide_by_zero"))
                instructions.add(Div)
                instructions.add(Move(rd, RegisterIterator.r0))
            }
            BinaryOperator.MOD -> {
                ErrorFuncs.visitDivideByZeroError()
                instructions.add(Move(RegisterIterator.r0, rd))
                instructions.add(Move(RegisterIterator.r1, rn))
                instructions.add(BranchWithLink("p_check_divide_by_zero"))
                instructions.add(Mod)
                instructions.add(Move(rd, RegisterIterator.r1))
            }
            BinaryOperator.PLUS -> {
                ErrorFuncs.visitOverflowError()
                instructions.add(Add(rd, rd, rn, Cond(Condition.AL), SFlag(True)))
                instructions.add(BranchWithLink("p_throw_overflow_error", Cond(Condition.VS)))
            }
            BinaryOperator.MINUS -> {
                ErrorFuncs.visitOverflowError()
                instructions.add(Subtract(rd, rd, rn, Cond(Condition.AL), SFlag(True)))
                instructions.add(BranchWithLink("p_throw_overflow_error", Cond(Condition.VS)))
            }
            BinaryOperator.GT -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.GT)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.LE)))
            }
            BinaryOperator.GTE -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.GE)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.LT)))
            }
            BinaryOperator.LT -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.LT)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.GE)))
            }
            BinaryOperator.LTE -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.LE)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.GT)))
            }
            BinaryOperator.EQUIV -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.EQ)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.NE)))
            }
            BinaryOperator.NOTEQUIV -> {
                instructions.add(Compare(rd, rn))
                instructions.add(Move(rd, Immediate(1), Cond(Condition.NE)))
                instructions.add(Move(rd, Immediate(0), Cond(Condition.EQ)))
            }
        }
        if (rn in regsInUse.first()) {
            regsInUse.first().remove(rn) //remove rn
            availableRegisters.add(rn)
        }

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
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(Move(rd, Immediate(0)))
    }

    override fun visitArrayLiteralNode(node: ArrayLiteral): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        // array type
        val typeSizeByte = node.type!!.getBaseType()!!.getByteSize()
        // size of array = size of pointer + (array type in byte * number of elements)
        val size = 4 + (typeSizeByte * node.value!!.size)
        instructions.add(Load(RegisterIterator.r0, Immediate(size)))
        //malloc allocates chunks of size byte and stores in r0
        instructions.add(BranchWithLink("malloc"))

        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        instructions.add(Move(rd, RegisterIterator.r0))

        val exprReg = availableRegisters.peek()
        for (i in 0..node.value!!.size - 1) {

            instructions.addAll(node.value!![i].accept(this))
            //+ 4 to adjust for length parameter of array
            val index = 4 + (i * typeSizeByte)

            //str/strb exprReg, [arrayPtr, #index]
            instructions.add(store(exprReg, ImmediateOffset(rd, Immediate(index)), typeSizeByte))

            //remove so they can be reused in the next iteration
            regsInUse.first().remove(exprReg)
            availableRegisters.add(exprReg)
        }

        val regArrayLen = availableRegisters.peek()
        instructions.add(load(regArrayLen, Immediate(node.value!!.size), Int.getByteSize()))
        instructions.add(store(regArrayLen, ZeroOffset(rd), Int.getByteSize()))

        //array pointer in rd
        return instructions
    }

    /* Code generation for unary operators. */

    override fun visitUnaryOpNode(node: UnaryOp): List<Instruction> {

        val instructions = mutableListOf<Instruction>()

        val rd = availableRegisters.peek()
        val exprInstr : List<Instruction> = node.e.accept(this)

        val unOpInstr : List<Instruction> = when (node.op) {
            UnaryOperator.CHR -> {
                listOf<Instruction>()
            }
            UnaryOperator.LEN -> {
                listOf<Instruction>(Load(rd, ZeroOffset(rd)))
            }
            UnaryOperator.ORD -> {
                listOf<Instruction>()
            }
            UnaryOperator.NEG -> {
                ErrorFuncs.visitOverflowError()
                listOf<Instruction>(
                        ReverseSubtract(rd, rd, Immediate(0), Cond(Condition.AL),SFlag(true)),
                        BranchWithLink("p_throw_overflow_error", Cond(Condition.VS))
                )
            }
            UnaryOperator.NOT -> {
                listOf<Instruction>(Xor(rd, rd, Immediate(1)))
            }
        }

        instructions.addAll(exprInstr)
        instructions.addAll(unOpInstr)

        return instructions
    }

    override fun visitPairElemNode(node: PairElem): List<Instruction> {
        val rd = availableRegisters.peek()
        val (childInstrs, loadable) = visitPairElemLhs(node)
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(childInstrs)
        if (node.text == "fst") {
            instructions.add(load(rd, ZeroOffset(rd), (node.e.type as PairInstance).t1!!.getByteSize()))
        } else {
            instructions.add(load(rd, loadable, (node.e.type as PairInstance).t2!!.getByteSize()))
        }
        return instructions
    }

    override fun visitPairElemLhs(node: PairElem): Pair<List<Instruction>, Loadable> {
        val rd = availableRegisters.peek()
        val childInstrs = node.e.accept(this)
        val instructions = mutableListOf<Instruction>()
        instructions.addAll(childInstrs)
        instructions.add(Move(RegisterIterator.r0, rd))
        instructions.add(BranchWithLink("p_check_null_pointer"))
        FreeFuncs.checkNullPointer()
        if (node.text == "fst") {
            instructions.add(Load(rd, ZeroOffset(rd)))
        } else {
            instructions.add(Load(rd, ImmediateOffset(rd, Immediate(4))))
        }
        return Pair(instructions, ZeroOffset(rd))
    }

    override fun visitVariableLhs(node: Variable): Pair<List<Instruction>, Loadable> {
        return Pair(listOf<Instruction>(), calcVarOffset(node.text))
    }

    override fun visitArrayElemLhs(node: ArrayElem): Pair<List<Instruction>, Loadable> {
        val instructions = mutableListOf<Instruction>()

        val base = availableRegisters.next()
        regsInUse.first().add(base)
        val loadable = calcVarOffset(node.id)
        var loadoffset = Immediate(0) 
        val reg: Register
        if (loadable is ZeroOffset) {
            reg = loadable.r
        } else {
            loadoffset = (loadable as ImmediateOffset).value
            reg = loadable.r
        }
        instructions.add(Add(base, reg, loadoffset))

        FreeFuncs.checkArrayBounds()
        val offset = availableRegisters.peek()
        for (i in 0..(node.dims - 2)) {
            //Evaluate expr i and store in offset
            instructions.addAll(node.values[i].accept(this))
            instructions.add(Load(base, ZeroOffset(base)))
            instructions.add(Move(RegisterIterator.r0, offset))
            instructions.add(Move(RegisterIterator.r1, base))
            instructions.add(BranchWithLink("p_check_array_bounds"))
            instructions.add(Add(base, base, Immediate(Int.getByteSize())))
            instructions.add(Add(base, base, ShiftOffset(offset, Immediate(2), Shift.LSL)))
            regsInUse.first().remove(offset)
            availableRegisters.add(offset)
        }
        instructions.addAll(node.values[node.dims - 1].accept(this))
        instructions.add(Load(base, ZeroOffset(base)))
        instructions.add(Move(RegisterIterator.r0, offset))
        instructions.add(Move(RegisterIterator.r1, base))
        instructions.add(BranchWithLink("p_check_array_bounds"))
        instructions.add(Add(base, base, Immediate(Int.getByteSize())))
        if (node.type()!!.getBaseType()!!.getByteSize() == 1) {
            instructions.add(Add(base, base, offset))
        } else {
            instructions.add(Add(base, base, ShiftOffset(offset, Immediate(2), Shift.LSL)))
        }

        regsInUse.first().remove(offset)
        availableRegisters.add(offset)

        return Pair(instructions, ZeroOffset(base))
    }

    override fun visitAssignSideIf(node : SideIf): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val elseLabel = Label()
        val endLabel = Label()

        val rd = availableRegisters.peek()
        instructions.addAll(node.cond.accept(this))
        instructions.add(Compare(rd, Immediate(0)))
        instructions.add(Branch(elseLabel.name, Cond(Condition.EQ)))

        // If true body
        instructions.addAll(node.exprIfTrue.accept(this))
        instructions.add(Branch(endLabel.name))

        // Else body
        instructions.add(elseLabel)
        instructions.addAll(node.exprIfFalse.accept(this))

        instructions.add(endLabel)
        return instructions
    }

    override fun visitSideEffectOp(node: SideEffectOp): List<Instruction> {
        val instructions = mutableListOf<Instruction>()

        val rd = availableRegisters.peek()
        val (childInstrs, loadable) = node.lhs.acceptLhs(this)

        instructions.addAll(childInstrs)

        val amt: Operand2
        if (node.incrAmount is IntLiteral) {
            amt = Immediate(node.incrAmount.value!!)
        } else {
            amt = availableRegisters.peek()
            instructions.addAll(node.incrAmount.accept(this))
        }

        when (node.op) {
            BinaryOperator.PLUS -> instructions.add(Add(rd, rd, amt))
            BinaryOperator.MINUS -> instructions.add(Subtract(rd, rd, amt))
            else -> throw Exception("Not Reachable")
        }

        instructions.add(store(rd, loadable, Int.getByteSize()))

        return instructions
    }

    override fun visitSideEffectOpLhs(node: SideEffectOp): Pair<List<Instruction>, Loadable> {
        return node.lhs.acceptLhs(this)
    }

}
