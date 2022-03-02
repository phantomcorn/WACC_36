package codegen.instr.register

import codegen.instr.InstructionVisitor

object PC : Register() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitPCRegister(this)
    }
}
