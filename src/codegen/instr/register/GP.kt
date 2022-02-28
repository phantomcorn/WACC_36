package codegen.instr.register

import codegen.instr.InstructionVisitor

class GP(val id: Int) : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitGPRegister(this)
    }
}