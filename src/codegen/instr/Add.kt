package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.register.Register

class Add(
    val Rd: Register,
    val Rn: Register,
    val operand2: Operand2,
    cond: Cond = Cond.AL,
    s: Boolean = false
) :
    Instruction(cond, s) {

    override fun accept(v: InstructionVisitor): String {
        return v.visitAdd(this)
    }
}
