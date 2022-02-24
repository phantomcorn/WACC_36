package codegen.instr

import codegen.instr.register.Register

class Load(val Rd: Register, val operand2: Operand2, cond: Cond = Cond.AL) : Instruction(cond) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLoad(this)
    }
}
