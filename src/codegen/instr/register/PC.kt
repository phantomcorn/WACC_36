package codegen.instr.register

import codegen.instr.InstructionVisitor

class PC(value: Int) : Register(value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPCRegister(this)
    }
}