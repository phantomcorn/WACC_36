package codegen.instr

import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class StoreByte(val Rd: Register, val operand: Loadable, cond: Cond = Cond.AL) : Instruction(cond) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitStoreByte(this)
    }
}
