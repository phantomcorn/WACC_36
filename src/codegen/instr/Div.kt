package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.register.Register

class Div(
    Rd : Register,
    Rn : Register,
    operand2 : Operand2,
    cond: Cond = Cond.AL,
    s: SBool = SBool(false)
) : Instruction(cond, s) {

    override fun accept(v: InstructionVisitor): String {
        return v.visitDiv(this)
    }


}
