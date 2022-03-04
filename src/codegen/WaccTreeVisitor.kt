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
import codegen.utils.SaveRegisters
import codegen.utils.PrintFuncs
import codegen.utils.FreeFuncs
import parse.semantics.SymbolTable
import parse.symbols.Int
import parse.symbols.Type
import parse.symbols.PairInstance
import parse.func.FuncAST
import codegen.instr.And
import codegen.instr.Or
import codegen.instr.Div
import codegen.instr.Mod
import parse.symbols.Boolean.True

enum class Error(val label: String) {
    OVERFLOW("p_throw_overflow_error") {
        override fun visitError() {
            if (WaccTreeVisitor.funcTable.lookup(this.label) != null) {
                return
            }
            val instr = mutableListOf<Instruction>()
            val overflowMsg = WaccTreeVisitor.stringTable.add("OverflowError: the result is too small/large to store in a 4-byte signed-integer.\\n\\0")
            instr.add(Load(GP(0), overflowMsg))
            instr.add(BranchWithLink(RUNTIME.label))

            val overflowFuncObj = FuncObj("")
            //Have to manually set name because errors do not begin with "f_"
            overflowFuncObj.funcName = this.label
            overflowFuncObj.funcBody.addAll(instr)
            WaccTreeVisitor.funcTable.add(this.label, overflowFuncObj)
            RUNTIME.visitError()
        }
    },
    RUNTIME("p_throw_runtime_error") {
        override fun visitError() {
            if (WaccTreeVisitor.funcTable.lookup(this.label) != null) {
                return
            }
            val instr = mutableListOf<Instruction>()
            instr.add(BranchWithLink("p_print_string"))
            instr.add(Move((GP(0)), Immediate(-1)))
            instr.add(BranchWithLink("exit"))

            val runtimeFuncObj = FuncObj("")
            //Have to manually set name because errors do not begin with "f_"
            runtimeFuncObj.funcName = this.label
            runtimeFuncObj.funcBody.addAll(instr)
            WaccTreeVisitor.funcTable.add(this.label, runtimeFuncObj)
            PrintFuncs.printString()
        }
    },
    DIVIDE_BY_ZERO("p_check_divide_by_zero") {
        override fun visitError() {
            if (WaccTreeVisitor.funcTable.lookup(this.label) != null) {
                return
            }

            val instr = mutableListOf<Instruction>()
            instr.add(Compare(GP(1), Immediate(0)))
            val msg = WaccTreeVisitor.stringTable.add("DivideByZeroError: divide or modulo by zero\\n\\0")
            instr.add(Load(GP(0), msg, Cond(Condition.EQ)))
            instr.add(BranchWithLink("p_throw_runtime_error", Cond(Condition.EQ)))

            val divideFuncObj = FuncObj("")
            //Have to manually set name because errors do not begin with "f_"
            divideFuncObj.funcName = this.label
            divideFuncObj.funcBody.addAll(instr)
            WaccTreeVisitor.funcTable.add(this.label, divideFuncObj)
            RUNTIME.visitError()
        }
    };
    abstract fun visitError()
}

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
        println(st.dict.keys)
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
        val instrs = mutableListOf<Instruction>()
        stackPush(symbolTable)
        instrs.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        instrs.addAll(root.accept(this))
        instrs.add(Add(SP, SP, Immediate(calcStackAlloc(symbolTable))))
        VariablePointer.pop()
        return instrs
    }

    override fun visitFunction(node: FuncAST) {
        var offset = 0
        for (i in 0..(node.params.values.size - 1)) {
	    //+4 to take return address on SP into account
	    variableST.add(node.params.values[i].paramName, Pair(4 + offset,0))
            offset += node.params.values[i].paramType!!.getByteSize()
        }
        val funcObj = funcTable.lookup(node.id)!!
        regsInUse.addFirst(mutableSetOf<Register>())
        val instrs = mutableListOf<Instruction>()

        regsInUse.addFirst(regsInUse.first())
        symbolTable = node.st
        variableST = SymbolTable(variableST)
        VariablePointer.push()
        offsetStack.addFirst(calcStackAlloc(symbolTable) - offset - node.st.lookup("$")!!.getByteSize())

        if (offsetStack.first() != 0) {
            instrs.add(Subtract(SP, SP, Immediate(offsetStack.first())))
            instrs.addAll(node.body.accept(this))
            instrs.add(Add(SP, SP, Immediate(offsetStack.removeFirst())))
	    instrs.add(Pop(listOf<Register>(PC)))
        } else {
            instrs.addAll(node.body.accept(this))
            offsetStack.removeFirst()
        }
        VariablePointer.pop()

        funcObj.funcBody.addAll(instrs)
        funcObj.user = true
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
        if (offsetStack.first() != 0) {
            result.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        }
        result.addAll(node.s.accept(this))
        if (offsetStack.first() != 0) {
            result.add(Add(SP, SP, Immediate(offsetStack.first())))
        }
        stackPop()
        result.add(condLabel)
        val rd = availableRegisters.peek()
        result.addAll(node.e.accept(this))
        result.add(Compare(rd, Immediate(1)))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        result.add(Branch(bodyLabel.name, Cond(Condition.EQ)))

        return result
    }

    override fun visitDeclarationNode(node: Declaration): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()

        VariablePointer.decrement(node.t.getByteSize())
        variableST.add(node.id, kotlin.Pair(VariablePointer.getCurrentOffset(), VariablePointer.level()))

        result.addAll(node.rhs.accept(this))

        //rd holds rhs value
        val index = calcVarOffset(node.id)
        result.add(store(rd, index, node.t.getByteSize()))

        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return result
    }

    override fun visitAssignmentNode(node: Assignment): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.rhs.accept(this))

        val rn = availableRegisters.peek()
        val (lhsInstrs, lhsLoadable) = node.lhs.acceptLhs(this)
        result.addAll(lhsInstrs)
        result.add(store(rd, lhsLoadable, node.rhs.type()!!.getByteSize()))

        availableRegisters.add(rn)
        regsInUse.first().remove(rn)
        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return result
    }

    override fun visitReadNode(node: Read): List<Instruction> {
        val result = mutableListOf<Instruction>()

        val (instrs, dest) = node.lhs.acceptLhs(this)
        result.addAll(instrs)
        var offset = Immediate(0) 
        var reg: Register
        if (dest is ZeroOffset) {
            reg = dest.r
        } else {
            offset = (dest as ImmediateOffset).value
            reg = dest.r
        }
        result.add(Add(RegisterIterator.r0, reg, offset))
    
        val (label, msg) = readLabel(node.lhs.type()!!)
        result.add(BranchWithLink(label))

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
        return result
    }

    override fun visitExitNode(node: Exit): List<Instruction> {
        val instrs = mutableListOf<Instruction>()

        val rd = availableRegisters.peek()
        val exprInstr = node.e.accept(this)

        instrs.addAll(exprInstr)
        instrs.add(Move(RegisterIterator.r0, rd))
        instrs.add(BranchWithLink("exit"))

        availableRegisters.add(rd)
        regsInUse.first().add(rd)

        return instrs
    }

    override fun visitPrintNode(node: Print): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.e.accept(this))
        result.add(Move(RegisterIterator.r0, rd))

        val label = printLabel(node.e.type!!)
        result.add(BranchWithLink(label))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        return result
    }

    override fun visitPrintlnNode(node: Println): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.e.accept(this))
        result.add(Move(RegisterIterator.r0, rd))

        val label = printLabel(node.e.type!!)
        result.add(BranchWithLink(label))
        result.add(BranchWithLink("p_print_ln"))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        PrintFuncs.printLn()
        return result
    }

    override fun visitStatListNode(statList: StatList): List<Instruction> {
        return statList.list.flatMap { x -> x.accept(this) }
    }

    override fun visitFreeNode(node: Free): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.e.accept(this))
        result.add(Move(RegisterIterator.r0, rd))
        when(node.e.type()!!) {
            is parse.symbols.Array -> {
                result.add(BranchWithLink("p_free_array"))
                FreeFuncs.freeArray()
            }
            is parse.symbols.Pair -> {
                result.add(BranchWithLink("p_free_pair"))
                FreeFuncs.freePair()
            }
            else -> {
                throw Exception("Not Reached")
            }
        }
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)
        return result
    }

    override fun visitIfNode(node: If): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val endLabel = Label()
        val elseLabel = Label()

        val rd = availableRegisters.peek()
        result.addAll(node.e.accept(this))
        result.add(Compare(rd, Immediate(0)))
        result.add(Branch(elseLabel.name, Cond(Condition.EQ)))
        regsInUse.first().remove(rd)
        availableRegisters.add(rd)

        // start VariablePointer at 0 to determined how many byte to allocate for reg SP

        stackPush(node.st1)
        if (offsetStack.first() != 0) {
            result.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        }
        result.addAll(node.s1.accept(this))
        if (offsetStack.first() != 0) {
            result.add(Add(SP, SP, Immediate(offsetStack.first())))
        }
        stackPop()
        result.add(Branch(endLabel.name))
        result.add(elseLabel)

        stackPush(node.st2)
        if (offsetStack.first() != 0) {
            result.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        }
        result.addAll(node.s2.accept(this))
        if (offsetStack.first() != 0) {
            result.add(Add(SP, SP, Immediate(offsetStack.first())))
        }
        stackPop()

        result.add(endLabel)
        return result
    }

    override fun visitBeginNode(node: Begin): List<Instruction> {
        val result = mutableListOf<Instruction>()

        stackPush(node.st)
        if (offsetStack.first() != 0) {
            result.add(Subtract(SP, SP, Immediate(offsetStack.first())))
        }
        result.addAll(node.s.accept(this))
        if (offsetStack.first() != 0) {
            result.add(Add(SP, SP, Immediate(offsetStack.first())))
        }
        stackPop()

        return result
    }

    override fun visitReturnNode(node: Return): List<Instruction> {
        val rd = availableRegisters.peek()
        val result = mutableListOf<Instruction>()
        result.addAll(node.e.accept(this))
        result.add(Move(RegisterIterator.r0, rd))

        var sum = 0
        for (i in offsetStack) {
            sum += i
        }
        if (sum != 0) {
            result.add(Add(SP, SP, Immediate(sum)))
        }

        result.add(Pop(listOf<Register>(PC)))
        availableRegisters.add(rd)
        regsInUse.first().remove(rd)
        return result
    }

    override fun visitVariableNode(node: Variable): List<Instruction> {
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        return listOf<Instruction>(load(rd, calcVarOffset(node.text), node.type!!.getByteSize()))
    }

    override fun visitNewPairNode(node: NewPair): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        val rn = availableRegisters.peek()
        result.add(Load(RegisterIterator.r0, Immediate(8)))
        result.add(BranchWithLink("malloc"))
        result.add(Move(rd, RegisterIterator.r0))
        result.addAll(node.e1.accept(this))
        result.add(Load(RegisterIterator.r0, Immediate(node.e1.type!!.getByteSize())))
        result.add(BranchWithLink("malloc"))
        result.add(store(rn, ZeroOffset(RegisterIterator.r0), node.e1.type!!.getByteSize()))
        result.add(Store(RegisterIterator.r0, ZeroOffset(rd)))
        regsInUse.first().remove(rn)
        availableRegisters.add(rn)
        result.addAll(node.e2.accept(this))
        result.add(Load(RegisterIterator.r0, Immediate(node.e2.type!!.getByteSize())))
        result.add(BranchWithLink("malloc"))
        result.add(store(rn, ZeroOffset(RegisterIterator.r0), node.e2.type!!.getByteSize()))
        result.add(Store(RegisterIterator.r0, ImmediateOffset(rd, Immediate(4))))
        regsInUse.first().remove(rn)
        availableRegisters.add(rn)
        return result
    }

    override fun visitCallNode(node: Call): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val rd = availableRegisters.peek()
        var totalSize = 0
        val regsSaved = regsInUse.first().toList()
        SaveRegisters.saveRegisters(regsSaved)
        for (e in node.values.reversed()) {
            val sizeType = e.type!!.getByteSize()
            result.addAll(e.accept(this))
            result.add(store(rd, PreImmediateOffset(SP, Immediate(-e.type()!!.getByteSize())), e.type!!.getByteSize()))
            preImmOffset += sizeType
            availableRegisters.add(rd)
            totalSize += sizeType
        }
        result.add(BranchWithLink(funcTable.lookup(node.id)!!.funcName))
        result.add(Add(SP, SP, Immediate(totalSize)))
        result.add(Move(rd, RegisterIterator.r0))
        availableRegisters.next()
        regsInUse.first().add(rd)
        SaveRegisters.restoreRegisters(regsSaved)
        preImmOffset = 0
        return result
    }

    override fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral): List<Instruction> {
        val result = mutableListOf<Instruction>()
        result.add(Load(RegisterIterator.r0, Immediate(4)))
        result.add(BranchWithLink("malloc"))
        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        result.add(Move(rd, RegisterIterator.r0))
        val rn = availableRegisters.next()
        regsInUse.first().add(rn)
        result.add(Load(rn, Immediate(0)))
        result.add(Store(rn, ZeroOffset(rd)))
        availableRegisters.add(rn)
        regsInUse.first().remove(rn)
        return result
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
        val (instrs, loadable) = visitArrayElemLhs(node)
        val result = mutableListOf<Instruction>()
        result.addAll(instrs)
        result.add(load(rd, loadable, node.type()!!.getBaseType()!!.getByteSize()))
        return result
    }

    /* Code generation for binary operators. */

    override fun visitBinaryOp(node : BinaryOp): List<Instruction> {

        val instructions = mutableListOf<Instruction>()

        val rd = availableRegisters.next()
        val rn = availableRegisters.next()

        if (node.e1.weight > node.e2.weight) {
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
                Error.OVERFLOW.visitError()
                instructions.add(Multiply(rd, rn, rd, rn))
                instructions.add(Compare(rn, ShiftOffset(rd, Immediate(31), Shift.ASR)))
                instructions.add(BranchWithLink("p_throw_overflow_error", Cond(Condition.NE)))
            }
            BinaryOperator.DIV -> {
                Error.DIVIDE_BY_ZERO.visitError()
                instructions.add(Move(RegisterIterator.r0, rd))
                instructions.add(Move(RegisterIterator.r1, rn))
                instructions.add(BranchWithLink("p_check_divide_by_zero"))
                instructions.add(Div)
                instructions.add(Move(rd, RegisterIterator.r0))
            }
            BinaryOperator.MOD -> {
                Error.DIVIDE_BY_ZERO.visitError()
                instructions.add(Move(RegisterIterator.r0, rd))
                instructions.add(Move(RegisterIterator.r1, rn))
                instructions.add(BranchWithLink("p_check_divide_by_zero"))
                instructions.add(Mod)
                instructions.add(Move(rd, RegisterIterator.r1))
            }
            BinaryOperator.PLUS -> {
                Error.OVERFLOW.visitError()
                instructions.add(Add(rd, rd, rn, Cond(Condition.AL), SFlag(True)))
                instructions.add(BranchWithLink("p_throw_overflow_error", Cond(Condition.VS)))
            }
            BinaryOperator.MINUS -> {
                Error.OVERFLOW.visitError()
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
        regsInUse.first().remove(rn) //remove rn
        availableRegisters.add(rn)

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
        val result = mutableListOf<Instruction>()
        // array type
        val typeSizeByte = node.type!!.getBaseType()!!.getByteSize()
        // size of array = size of pointer + (array type in byte * number of elements)
        val size = 4 + (typeSizeByte * node.value!!.size)
        result.add(Load(RegisterIterator.r0, Immediate(size)))
        //malloc allocates chunks of size byte and stores in r0
        result.add(BranchWithLink("malloc"))

        val rd = availableRegisters.next()
        regsInUse.first().add(rd)
        result.add(Move(rd, RegisterIterator.r0))

        val exprReg = availableRegisters.peek()
        for (i in 0..node.value!!.size - 1) {

            result.addAll(node.value!![i].accept(this))
            //+ 4 to adjust for length parameter of array
            val index = 4 + (i * typeSizeByte)

            //str/strb exprReg, [arrayPtr, #index]
            result.add(store(exprReg, ImmediateOffset(rd, Immediate(index)), typeSizeByte))

            //remove so they can be reused in the next iteration
            regsInUse.first().remove(exprReg)
            availableRegisters.add(exprReg)
        }

        val regArrayLen = availableRegisters.peek()
        result.add(load(regArrayLen, Immediate(node.value!!.size), Int.getByteSize()))
        result.add(store(regArrayLen, ZeroOffset(rd), Int.getByteSize()))

        //array pointer in rd
        return result
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
                listOf<Instruction>(ReverseSubtract(rd, rd, Immediate(0)))
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
        val (instrs, loadable) = visitPairElemLhs(node)
        val result = mutableListOf<Instruction>()
        result.addAll(instrs)
        if (node.text == "fst") {
            result.add(load(rd, ZeroOffset(rd), (node.e.type as PairInstance).t1!!.getByteSize()))
        } else {
            result.add(load(rd, loadable, (node.e.type as PairInstance).t2!!.getByteSize()))
        }
        return result
    }

    override fun visitPairElemLhs(node: PairElem): Pair<List<Instruction>, Loadable> {
        val rd = availableRegisters.peek()
        val instrs = node.e.accept(this)
        val result = mutableListOf<Instruction>()
        result.addAll(instrs)
        result.add(Move(RegisterIterator.r0, rd))
        result.add(BranchWithLink("p_check_null_pointer"))
        FreeFuncs.checkNullPointer()
        if (node.text == "fst") {
            result.add(Load(rd, ZeroOffset(rd)))
        } else {
            result.add(Load(rd, ImmediateOffset(rd, Immediate(4))))
        }
        return Pair(result, ZeroOffset(rd))
    }

    override fun visitVariableLhs(node: Variable): Pair<List<Instruction>, Loadable> {
        return Pair(listOf<Instruction>(), calcVarOffset(node.text))
    }

    override fun visitArrayElemLhs(node: ArrayElem): Pair<List<Instruction>, Loadable> {
        val instrs = mutableListOf<Instruction>()

        val base = availableRegisters.next()
        regsInUse.first().add(base)
        val loadable = calcVarOffset(node.id)
        var loadoffset = Immediate(0) 
        var reg: Register
        if (loadable is ZeroOffset) {
            reg = loadable.r
        } else {
            loadoffset = (loadable as ImmediateOffset).value
            reg = loadable.r
        }
        instrs.add(Add(base, reg, loadoffset))

        FreeFuncs.checkArrayBounds()
        val offset = availableRegisters.peek()
        for (i in 0..(node.dims - 2)) {
            //Evaluate expr i and store in offset
            instrs.addAll(node.values[i].accept(this))
            instrs.add(Load(base, ZeroOffset(base)))
            instrs.add(Move(RegisterIterator.r0, offset))
            instrs.add(Move(RegisterIterator.r1, base))
            instrs.add(BranchWithLink("p_check_array_bounds"))
            instrs.add(Add(base, base, Immediate(Int.getByteSize())))
            instrs.add(Add(base, base, ShiftOffset(offset, Immediate(2), Shift.LSL)))
            regsInUse.first().remove(offset)
            availableRegisters.add(offset)
        }
        instrs.addAll(node.values[node.dims - 1].accept(this))
        instrs.add(Load(base, ZeroOffset(base)))
        instrs.add(Move(RegisterIterator.r0, offset))
        instrs.add(Move(RegisterIterator.r1, base))
        instrs.add(BranchWithLink("p_check_array_bounds"))
        instrs.add(Add(base, base, Immediate(Int.getByteSize())))
        if (node.type()!!.getBaseType()!!.getByteSize() == 1) {
            instrs.add(Add(base, base, offset))
        } else {
            instrs.add(Add(base, base, ShiftOffset(offset, Immediate(2), Shift.LSL)))
        }

        regsInUse.first().remove(offset)
        availableRegisters.add(offset)

        return Pair(instrs, ZeroOffset(base))
    }
}
