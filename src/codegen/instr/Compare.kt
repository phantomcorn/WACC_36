package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.operand2.register.Register

class Compare(
    private val Rn : Register,
    private val operand2 : Operand2,
    cond: Cond = Cond.AL,
    s: Boolean = false
) : Instruction(cond, s) {


    override fun accept(v: InstructionVisitor): String {
        return v.visitCompare(this)
    }
}
