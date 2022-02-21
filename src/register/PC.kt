package register

import instr.InstructionVisitor

class PC (value: Int) : Register(value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPCRegister(this)
    }
}