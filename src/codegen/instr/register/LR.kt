package codegen.instr.register

import codegen.instr.InstructionVisitor

object LR : Register() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLRRegister(this)
    }
}
