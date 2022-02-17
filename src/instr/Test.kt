package instr

import register.Register

class Test(val Rn: Register, val Op2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitTest(Rn, Op2)
    }
}
