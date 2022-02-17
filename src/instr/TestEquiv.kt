package instr

import register.Register

class TestEquiv(val Rn: Register, val operand2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitTestEquiv(Rn, operand2)
    }
}
