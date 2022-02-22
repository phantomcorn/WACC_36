package codegen.instr

import codegen.instr.register.Register

class And(
    val Rd: Register,
    val Rn: Register,
    val operand2: Operand2?,
    cond: Cond = Cond.AL,
    s: Boolean = false
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitAnd(this)
    }
}
