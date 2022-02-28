package codegen.instr

import codegen.instr.register.Register

class Multiply(
    val Rd: Register,
    val Rm: Register,
    val Rs: Register,
    cond: Cond = Cond.AL,
    s: SBool = SBool(false)
) :
    Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMul(this)
    }
}
