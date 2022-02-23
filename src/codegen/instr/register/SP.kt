package codegen.instr.register

import codegen.instr.InstructionVisitor

class SP(value: Int) : Register(value) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitSPRegister(this)
    }
}