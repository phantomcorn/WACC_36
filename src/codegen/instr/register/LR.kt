package codegen.instr.register

import codegen.instr.InstructionVisitor

object LR : Register() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitLRRegister(this)
    }
}
