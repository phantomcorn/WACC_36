package instr
import register.*

import register.Register

interface InstructionVisitor {
    fun visitTest(x: Test): String
    fun visitTestEquiv(x: TestEquiv): String
    fun visitAnd(x: And): String
    fun visitXor(x: Xor): String
    fun visitOr(x: Or): String
    fun visitAdd(x: Add): String
    fun visitSub(x: Subtract): String
    fun visitMul(x: Multiply): String
    fun visitBranch(x: Branch): String
    fun visitBranchWithLink(x: Branch): String
    fun visitMove(x: Move): String
    fun visitCompare(x: Compare): String
    fun visitLoad(x: Load): String
    fun visitLoadByte(x: LoadByte): String
    fun visitStore(x: Store): String
    fun visitStoreByte(x: StoreByte): String
}
