package codegen.instr.register

import codegen.instr.InstructionVisitor

class LR() : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLRRegister(this)
    }
}