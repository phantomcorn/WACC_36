package codegen

import codegen.instr.*
import codegen.instr.arm.ARMCond
import codegen.instr.arm.ARMS
import codegen.instr.operand2.Immediate
import codegen.instr.operand2.ImmediateChar
import codegen.instr.register.GP
import codegen.instr.register.LR
import codegen.instr.register.PC
import codegen.instr.register.SP

class ARMInstructionVisitor : InstructionVisitor {
    override fun visitTest(x: Test): String {
        TODO("Not yet implemented")
    }

    override fun visitTestEquiv(x: TestEquiv): String {
        TODO("Not yet implemented")
    }

    override fun visitAnd(x: And): String {
        TODO("Not yet implemented")
    }

    override fun visitXor(x: Xor): String {
        TODO("Not yet implemented")
    }

    override fun visitOr(x: Or): String {
        return "ORR${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}"
    }

    override fun visitAdd(x: Add): String {
        return "ADD${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}"
    }

    override fun visitSub(x: Subtract): String {
        return "SUB${ARMCond.accept(x.cond)}${ARMS.accept(x.s)} ${x.Rd.accept(this)}, ${x.Rn.accept(this)}, ${x.operand2.accept(this)}"
    }

    override fun visitMul(x: Multiply): String {
        TODO("Not yet implemented")
    }

    override fun visitBranch(x: Branch): String {
        TODO("Not yet implemented")
    }

    override fun visitBranchWithLink(x: BranchWithLink): String {
        TODO("Not yet implemented")
    }

    override fun visitMove(x: Move): String {
        TODO("Not yet implemented")
    }

    override fun visitCompare(x: Compare): String {
        TODO("Not yet implemented")
    }

    override fun visitLoad(x: Load): String {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun visitPop(x: Pop): String {
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
        return "#" + java.lang.Integer.toHexString(x.value)
    }

    override fun visitImmediateChar(x: ImmediateChar): String {
        return "#" + x.value
    }
}
