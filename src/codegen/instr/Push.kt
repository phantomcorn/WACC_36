package codegen.instr

import codegen.instr.register.Register

class Push(val src : Register) : Instruction() {

    override fun accept(v: InstructionVisitor): String {
        return v.visitPush(this)
    }
}