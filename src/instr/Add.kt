package instr

import register.Register

class Add(val Rd: Register, val Rn: Register, val operand2: Operand2, cond: Cond = Cond.AL, s: Boolean = false) :
    Instruction(cond, s) {

    override fun accept(v: InstructionVisitor): String {
        return v.visitAdd(Rd, Rn, operand2)
    }
}