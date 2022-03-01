package codegen.instr.register

import codegen.instr.InstructionVisitor

object SP : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSPRegister(this)
    }
}
