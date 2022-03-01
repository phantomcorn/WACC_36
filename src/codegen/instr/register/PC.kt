package codegen.instr.register

import codegen.instr.InstructionVisitor

object PC : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPCRegister(this)
    }
}
