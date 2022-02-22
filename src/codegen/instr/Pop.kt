package codegen.instr

import codegen.instr.register.Register

class Pop(val dest: Register) : Instruction() {

    override fun accept(v: InstructionVisitor): String {
        return v.visitPop(this)
    }
}