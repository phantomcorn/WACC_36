package codegen.instr

import codegen.instr.register.Register

class Multiply(
    val RdHi: Register,
    val RdLo: Register,
    val Rm: Register,
    val Rs: Register,
    cond: Cond = Cond(Condition.AL),
    s: SFlag = SFlag(false)
) :
    Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMul(this)
    }
}
