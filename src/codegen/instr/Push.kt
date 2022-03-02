package codegen.instr

import codegen.instr.register.Register

class Push(val reglist: List<Register>) : Instruction() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitPush(this)
    }
}
