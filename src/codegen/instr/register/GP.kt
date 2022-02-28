package codegen.instr.register

import codegen.instr.InstructionVisitor

class GP(val id: Int) : Register(0) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitGPRegister(this)
    }
}