package instr

import register.Register

class Xor(val Rd: Register, val Rn: Register, val Op2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitXor(Rd, Rn, Op2)
    }
}
