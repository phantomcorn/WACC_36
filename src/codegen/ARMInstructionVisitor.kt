package codegen

import codegen.instr.*
import codegen.instr.arm.ARMCond
import codegen.instr.arm.ARMS
import codegen.instr.operand2.Immediate
import codegen.instr.operand2.ImmediateChar
import codegen.instr.operand2.ImmediateOffset
import codegen.instr.operand2.PreImmediateOffset
import codegen.instr.operand2.ZeroOffset
import codegen.instr.operand2.RegisterOffset
import codegen.instr.operand2.PreRegisterOffset
import codegen.instr.loadable.Msg
import codegen.instr.register.GP
import codegen.instr.register.LR
import codegen.instr.register.PC
import codegen.instr.register.SP
import codegen.utils.SaveRegisters

class ARMInstructionVisitor : InstructionVisitor {
    override fun translateInstructions(instr: List<Instruction>): String {
        return instr.map { instruction -> instruction.accept(this) }.reduce { instr1, instr2 -> instr1 + "\n" + instr2 }
    }

    override fun visitTest(x: Test): String {
        return "TST${ARMCond.accept(x.cond)} ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitTestEquiv(x: TestEquiv): String {
        return "TEQ${ARMCond.accept(x.cond)} ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitAnd(x: And): String {
        return "AND${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitXor(x: Xor): String {
        return "XOR${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitOr(x: Or): String {
        return "ORR${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitAdd(x: Add): String {
        return "ADD${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitSub(x: Subtract): String {
        return "SUB${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitRevSub(x: ReverseSubtract): String {
        return "RSB${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitMul(x: Multiply): String {
        return "SMULL${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.RdHi.accept(this)},${x.RdLo.accept(this)}, ${x.Rm.accept(this)}, ${x.Rs.accept(this)}\n"
    }

    override fun visitBranch(x: Branch): String {
        return "B${ARMCond.accept(x.cond)} ${x.dest}\n"
    }

    override fun visitBranchWithLink(x: BranchWithLink): String {
        return "BL${ARMCond.accept(x.cond)} ${x.dest}\n"
    }

    override fun visitMove(x: Move): String {
        return "MOV${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitCompare(x: Compare): String {
        return "CMP${ARMCond.accept(x.cond)} ${x.Rn.accept(this)}, ${x.operand2.accept(this)}\n"
    }

    override fun visitLoad(x: Load): String {
        return "LDR${ARMCond.accept(x.cond)} ${x.Rd.accept(this)}, ${x.operand.loadAccept(this)}"
    }

    override fun visitLoadByte(x: LoadByte): String {
        TODO("Not yet implemented")
    }

    override fun visitStore(x: Store): String {
        TODO("Not yet implemented")
    }

    override fun visitStoreByte(x: StoreByte): String {
        TODO("Not yet implemented")
    }

    override fun visitPush(x: Push): String {
        return "PUSH ${SaveRegisters.formatRegList(x.reglist, this)}"
    }

    override fun visitPop(x: Pop): String {
        return "POP ${SaveRegisters.formatRegList(x.reglist, this)}"
    }

    override fun visitMod(x: Mod): String {
        TODO("Not yet implemented")
    }

    override fun visitDiv(x: Div): String {
        TODO("Not yet implemented")
    }

    override fun visitGPRegister(x: GP): String {
        return "r" + x.id.toString()
    }

    override fun visitPCRegister(x: PC): String {
        return "r15"
    }

    override fun visitLRRegister(x: LR): String {
        return "r14"
    }

    override fun visitSPRegister(x: SP): String {
        return "r13"
    }

    override fun visitImmediate(x: Immediate): String {
        return "#0x" + Integer.toHexString(x.value)
    }

    override fun visitImmediateChar(x: ImmediateChar): String {
        return "#" + x.value
    }

    override fun visitLabel(x: Label): String {
        return x.name + ":"
    }

    override fun loadImmediate(x: Immediate): String {
        return "=" + Integer.toHexString(x.value)
    }

    override fun loadMsg(x: Msg): String {
        return "=" + x.s
    }

    override fun visitImmediateOffset(x: ImmediateOffset): String {
        TODO("Not Implemented")
    }

    override fun loadImmediateOffset(x: ImmediateOffset): String {
        TODO("Not Implemented")
    }

    override fun visitZeroOffset(x: ZeroOffset): String {
        TODO("Not Implemented")
    }

    override fun loadZeroOffset(x: ZeroOffset): String {
        TODO("Not Implemented")
    }

    override fun visitRegisterOffset(x: RegisterOffset): String {
        TODO("Not Implemented")
    }

    override fun loadRegisterOffset(x: RegisterOffset): String {
        TODO("Not Implemented")
    }

    override fun visitPreRegisterOffset(x: PreRegisterOffset): String {
        TODO("Not Implemented")
    }

    override fun loadPreRegisterOffset(x: PreRegisterOffset): String {
        TODO("Not Implemented")
    }

    override fun visitPreImmediateOffset(x: PreImmediateOffset): String {
        TODO("Not Implemented")
    }

    override fun loadPreImmediateOffset(x: PreImmediateOffset): String {
        TODO("Not Implemented")
    }
}
