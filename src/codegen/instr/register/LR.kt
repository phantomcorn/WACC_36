package codegen.instr.register

import codegen.instr.InstructionVisitor

class LR(value: Int) : Register(value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLRRegister(this)
    }
}