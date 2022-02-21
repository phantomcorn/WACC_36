package register

import instr.InstructionVisitor

class SP (value: Int): Register (value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSPRegister(this)
    }
}