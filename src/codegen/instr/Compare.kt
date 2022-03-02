package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.register.Register

class Compare(
    val Rn: Register,
    val operand2: Operand2,
    cond: Cond = Cond(Condition.AL),
    s: SFlag = SFlag(false)
) : Instruction(cond, s) {


    override fun accept(v: InstructionVisitor): String {
        return v.visitCompare(this)
    }
}
