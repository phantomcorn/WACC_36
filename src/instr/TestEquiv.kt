package instr

import register.Register

class TestEquiv(val Rn: Register, val Op2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitTestEquiv(Rn, Op2)
    }
}
