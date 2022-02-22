package codegen.instr

import codegen.instr.operand2.Operand2
import codegen.instr.operand2.register.Register

class Test(val Rn: Register, val operand2: Operand2) : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitTest(this)
    }
}
