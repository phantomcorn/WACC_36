package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.register.Register

class Subtract(
    val Rd: Register,
    val Rn: Register,
    val operand2: Operand2,
    cond: Cond = Cond(Condition.AL),
    s: SBool = SBool(false)
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSub(this)
    }
}
