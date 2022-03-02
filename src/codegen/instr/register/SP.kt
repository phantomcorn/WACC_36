package codegen.instr.register

import codegen.instr.InstructionVisitor

object SP : Register() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitSPRegister(this)
    }
}
