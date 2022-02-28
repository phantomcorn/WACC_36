package codegen.instr.register

import codegen.instr.InstructionVisitor

object SP : Register(0) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSPRegister(this)
    }


}