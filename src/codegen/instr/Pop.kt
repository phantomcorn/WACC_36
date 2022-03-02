package codegen.instr

import codegen.instr.register.Register

class Pop(val reglist: List<Register>) : Instruction() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitPop(this)
    }
}
