package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.register.Register

class Move(
    val Rd: Register,
    val operand2: Operand2,
    cond: Cond = Cond.AL,
    s: SBool = SBool(false)
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMove(this)
    }
}
