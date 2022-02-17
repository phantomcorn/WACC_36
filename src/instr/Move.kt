package instr

import register.Register

class Move(
    val Rd: Register, 
    val operand2: Operand2,
    cond: Cond = Cond.AL,
    s: Boolean = false
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMove(Rd, operand2)
    }
}
