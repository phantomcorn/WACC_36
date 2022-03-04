package codegen.instr.register

import codegen.instr.InstructionVisitor

class GP(val id: Int) : Register() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitGPRegister(this)
    }
}
