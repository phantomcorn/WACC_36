package codegen.instr.register

import codegen.instr.InstructionVisitor

class PC() : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPCRegister(this)
    }
}