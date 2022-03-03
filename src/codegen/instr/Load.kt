package codegen.instr

import codegen.instr.register.Register
import codegen.instr.loadable.Loadable

class Load(val Rd: Register, val operand: Loadable, cond: Cond = Cond(Condition.AL)) : Instruction(cond) {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitLoad(this)
    }
}
