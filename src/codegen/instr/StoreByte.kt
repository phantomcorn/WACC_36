package codegen.instr

import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class StoreByte(val Rd: Register, val operand: Loadable, cond: Cond = Cond(Condition.AL)) : Instruction(cond) {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitStoreByte(this)
    }
}
