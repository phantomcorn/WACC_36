package codegen.instr

import codegen.instr.register.Register

class TestEquiv(val Rn: Register, val operand2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitTestEquiv(this)
    }
}
