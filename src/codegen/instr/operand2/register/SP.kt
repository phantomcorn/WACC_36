package codegen.instr.operand2.register

import codegen.instr.InstructionVisitor

class SP (value: Int): Register (value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSPRegister(this)
    }
}