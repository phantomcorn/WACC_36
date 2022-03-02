package codegen

import codegen.instr.*
import codegen.instr.loadable.*
import codegen.instr.operand2.*
import codegen.instr.register.*

class AllocRegPass(val maxReg: Int) : InstructionVisitor<List<Instruction>> {
    val MAX_REG = 10
    val regSpace = parse.symbols.Int.getByteSize() * (maxReg - MAX_REG)
    var currentOffset = regSpace
    var regAllocated = false
    override fun visitInstructions(instrs: List<Instruction>): List<Instruction> {
        if (maxReg < MAX_REG || instrs.size < 2) {
            return instrs
        }
        val result = mutableListOf<Instruction>(instrs[0])
        result.add(Subtract(SP, SP, Immediate(regSpace)))
        for (i in 1..(instrs.size - 1)) {
            result.addAll(instrs[i].accept<List<Instruction>>(this))
        }
        result.add(Add(SP, SP, Immediate(regSpace)))
        return result
    }

    fun calcRegAddress(r: Int): ImmediateOffset {
        return ImmediateOffset(SP, Immediate(currentOffset + (parse.symbols.Int.getByteSize() * (r - MAX_REG))))
    }

    fun visitOperand2(operand2: Operand2): Operand2 {
        when (operand2) {
            is ZeroOffset -> {
                if (operand2.r is GP && operand2.r.id >= MAX_REG) {
                    return calcRegAddress(operand2.r.id)
                } else {
                    return operand2
                }
            }
            is ImmediateOffset -> {
                if (operand2.r is GP && operand2.r.id >= MAX_REG) {
                    val regAddress = calcRegAddress(operand2.r.id)
                    return ImmediateOffset(SP, Immediate(regAddress.value.value + operand2.value.value))
                } else {
                    return operand2
                }
            }
            is Register -> {
                if (operand2 is GP && operand2.id >= MAX_REG) {
                    return calcRegAddress(operand2.id)
                } else {
                    return operand2
                }
            }
            else -> {
                return operand2
            }
        }
    }

    fun visitLoadable(l: Loadable): Loadable {
        when (l) {
            is ZeroOffset -> {
                if (l.r is GP && l.r.id >= MAX_REG) {
                    return calcRegAddress(l.r.id)
                } else {
                    return l
                }
            }
            is ImmediateOffset -> {
                if (l.r is GP && l.r.id >= MAX_REG) {
                    val regAddress = calcRegAddress(l.r.id)
                    return ImmediateOffset(SP, Immediate(regAddress.value.value + l.value.value))
                } else {
                    return l
                }
            }
            else -> {
                return l
            }
        }
    }

    data class RegResult(val pre: List<Instruction>, val post: List<Instruction>, val r: Register, val k: Int)
    fun visitRegister(r: Register): RegResult {
        when (r) {
            is GP -> {
                if (r.id >= MAX_REG) {
                    val rAddr = calcRegAddress(r.id)
                    var r2 = GP(MAX_REG)
                    if (regAllocated) {
                        r2 = GP(MAX_REG + 1)
                    } else {
                        regAllocated = true
                    }
                    currentOffset -= parse.symbols.Int.getByteSize();
                    return RegResult(listOf(Push(listOf(r2)), Load(r2, rAddr)), listOf(Store(r2, rAddr), Pop(listOf(r2))), r2, parse.symbols.Int.getByteSize())
                } else {
                    return RegResult(listOf(), listOf(), r, 0)
                }
            }
            else -> return RegResult(listOf(), listOf(), r, 0)
        }
    }


    override fun visitTest(x: Test): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k) = visitRegister(x.Rn)
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.add(Test(rd, operand2))
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitTestEquiv(x: TestEquiv): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k) = visitRegister(x.Rn)
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.add(TestEquiv(rd, operand2))
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitAnd(x: And): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k1) = visitRegister(x.Rd)
        val (rnPre, rnPost, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(And(rd, rn, operand2, x.cond, x.s))
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitXor(x: Xor): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k1) = visitRegister(x.Rd)
        val (rnPre, rnPost, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(Xor(rd, rn, operand2, x.cond, x.s))
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitOr(x: Or): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k1) = visitRegister(x.Rd)
        val (rnPre, rnPost, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(Or(rd, rn, operand2, x.cond, x.s))
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitAdd(x: Add): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, _, rd, k1) = visitRegister(x.Rd)
        val (rnPre, _, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(Add(rd, rn, operand2, x.cond, x.s))
        if (rd is SP && operand2 is Immediate) {
            currentOffset += operand2.value
        }
        val (_, rdPost, _, _) = visitRegister(x.Rd)
        val (_, rnPost, _, _) = visitRegister(x.Rn)
        regAllocated = false
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitSub(x: Subtract): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, _, rd, k1) = visitRegister(x.Rd)
        val (rnPre, _, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(Subtract(rd, rn, operand2, x.cond, x.s))
        if (rd is SP && operand2 is Immediate) {
            currentOffset -= operand2.value
        }
        val (_, rdPost, _, _) = visitRegister(x.Rd)
        val (_, rnPost, _, _) = visitRegister(x.Rn)
        regAllocated = false
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitRevSub(x: ReverseSubtract): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k1) = visitRegister(x.Rd)
        val (rnPre, rnPost, rn, k2) = visitRegister(x.Rn)
        val k = k1 + k2
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(ReverseSubtract(rd, rn, operand2, x.cond, x.s))
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitMul(x: Multiply): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k1) = visitRegister(x.RdHi)
        val (rnPre, rnPost, rn, k2) = visitRegister(x.RdLo)
        val k = k1 + k2
        regAllocated = false
        result.addAll(rdPre)
        result.addAll(rnPre)
        result.add(Multiply(rd, rn, rd, rn, x.cond, x.s))
        result.addAll(rnPost)
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitBranch(x: Branch): List<Instruction> {
        return listOf(x)
    }

    override fun visitBranchWithLink(x: BranchWithLink): List<Instruction> {
        return listOf(x)
    }

    override fun visitMove(x: Move): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k) = visitRegister(x.Rd)
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.add(Move(rd, operand2, x.cond, x.s))
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitCompare(x: Compare): List<Instruction> {
        val result = mutableListOf<Instruction>()
        val (rdPre, rdPost, rd, k) = visitRegister(x.Rn)
        regAllocated = false
        val operand2 = visitOperand2(x.operand2)
        result.addAll(rdPre)
        result.add(Compare(rd, operand2, x.cond, x.s))
        result.addAll(rdPost)
        currentOffset += k
        return result
    }

    override fun visitLoad(x: Load): List<Instruction> {
        val result = mutableListOf<Instruction>()
        if (x.Rd is GP && x.Rd.id >= MAX_REG) {
            result.add(Push(listOf(GP(MAX_REG))))
            currentOffset -= 4
            val loadable = visitLoadable(x.operand)
            result.add(Load(GP(MAX_REG), loadable, x.cond))
            result.add(Store(GP(MAX_REG), calcRegAddress(x.Rd.id)))
            result.add(Pop(listOf(GP(MAX_REG))))
            currentOffset += 4
        } else {
            val loadable = visitLoadable(x.operand)
            result.add(Load(x.Rd, loadable, x.cond))
        }
        return result
    }

    override fun visitLoadByte(x: LoadByte): List<Instruction> {
        val result = mutableListOf<Instruction>()
        if (x.Rd is GP && x.Rd.id >= MAX_REG) {
            result.add(Push(listOf(GP(MAX_REG))))
            val loadable = visitLoadable(x.operand)
            currentOffset -= 4
            result.add(LoadByte(GP(MAX_REG), loadable, x.cond))
            result.add(StoreByte(GP(MAX_REG), calcRegAddress(x.Rd.id)))
            result.add(Pop(listOf(GP(MAX_REG))))
            currentOffset += 4
        } else {
            val loadable = visitLoadable(x.operand)
            result.add(LoadByte(x.Rd, loadable, x.cond))
        }
        return result
    }

    override fun visitStore(x: Store): List<Instruction> {
        val result = mutableListOf<Instruction>()
        if (x.Rd is GP && x.Rd.id >= MAX_REG) {
            result.add(Push(listOf(GP(MAX_REG))))
            currentOffset -= 4
            val loadable = visitLoadable(x.operand)
            result.add(Load(GP(MAX_REG), loadable, x.cond))
            result.add(Store(GP(MAX_REG), calcRegAddress(x.Rd.id)))
            result.add(Pop(listOf(GP(MAX_REG))))
            currentOffset += 4
        } else {
            val loadable = visitLoadable(x.operand)
            result.add(Store(x.Rd, loadable, x.cond))
        }
        return result

    }

    override fun visitStoreByte(x: StoreByte): List<Instruction> {
        val result = mutableListOf<Instruction>()
        if (x.Rd is GP && x.Rd.id >= MAX_REG) {
            result.add(Push(listOf(GP(MAX_REG))))
            currentOffset -= 4
            val loadable = visitLoadable(x.operand)
            result.add(LoadByte(GP(MAX_REG), loadable, x.cond))
            result.add(StoreByte(GP(MAX_REG), calcRegAddress(x.Rd.id)))
            result.add(Pop(listOf(GP(MAX_REG))))
            currentOffset += 4
        } else {
            val loadable = visitLoadable(x.operand)
            result.add(StoreByte(x.Rd, loadable, x.cond))
        }
        return result

    }

    override fun visitPush(x: Push): List<Instruction> {
        return listOf(x)
    }

    override fun visitPop(x: Pop): List<Instruction> {
        return listOf(x)
    }

    override fun visitMod(x: Mod): List<Instruction> {
        return listOf(x)
    }

    override fun visitDiv(x: Div): List<Instruction> {
        return listOf(x)
    }

    // partial visitor functions (returns part of an instruction, not a complete instruction)

    override fun visitGPRegister(x: GP): List<Instruction> {
        TODO()
    }

    override fun visitPCRegister(x: PC): List<Instruction> {
        TODO()
    }

    override fun visitLRRegister(x: LR): List<Instruction> {
        TODO()
    }

    override fun visitSPRegister(x: SP): List<Instruction> {
        TODO()
    }

    override fun visitImmediate(x: Immediate): List<Instruction> {
        TODO()
    }

    override fun visitImmediateChar(x: ImmediateChar): List<Instruction> {
        TODO()
    }

    override fun visitLabel(x: Label): List<Instruction> {
        TODO()
    }

    override fun loadImmediate(x: Immediate): List<Instruction> {
        TODO()
    }

    override fun loadMsg(x: Msg): List<Instruction> {
        TODO()
    }

    override fun visitImmediateOffset(x: ImmediateOffset): List<Instruction> {
        TODO()
    }

    override fun loadImmediateOffset(x: ImmediateOffset): List<Instruction> {
        TODO()
    }

    override fun visitZeroOffset(x: ZeroOffset): List<Instruction> {
        TODO()
    }

    override fun loadZeroOffset(x: ZeroOffset): List<Instruction> {
        TODO()
    }

    override fun visitRegisterOffset(x: RegisterOffset): List<Instruction> {
        TODO()
    }

    override fun loadRegisterOffset(x: RegisterOffset): List<Instruction> {
        TODO()
    }

    override fun visitPreRegisterOffset(x: PreRegisterOffset): List<Instruction> {
        TODO()
    }

    override fun loadPreRegisterOffset(x: PreRegisterOffset): List<Instruction> {
        TODO()
    }

    override fun visitPreImmediateOffset(x: PreImmediateOffset): List<Instruction> {
        TODO()
    }

    override fun loadPreImmediateOffset(x: PreImmediateOffset): List<Instruction> {
        TODO()
    }

    override fun visitShiftOffset(x: ShiftOffset): List<Instruction> {
        TODO()
    }

    override fun loadShiftOffset(x: ShiftOffset): List<Instruction> {
        TODO()
    }

    override fun visitSBool(x: SFlag): List<Instruction> {
        TODO()
    }

    override fun visitCond(x: Cond): List<Instruction> {
        TODO()
    }
}
