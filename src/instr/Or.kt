package instr

import register.Register

class Or(val Rd: Register, val Rn: Register, val Op2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitOr(Rd, Rn, Op2)
    }
}
