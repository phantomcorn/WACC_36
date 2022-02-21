package codegen

import instr.*
import register.*

class WaccInstructionVisitor: InstructionVisitor {
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
        TODO("Not yet implemented")
    }

    override fun visitAdd(x: Add): String {
        TODO("Not yet implemented")
    }

    override fun visitSub(x: Subtract): String {
        TODO("Not yet implemented")
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
}