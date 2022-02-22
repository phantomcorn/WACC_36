package codegen.instr

import codegen.instr.register.Register

class Pop(val reglist: List<Register>) : Instruction(Cond.AL, false) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPop(this)
    }
}
