package codegen.instr

import codegen.instr.register.Register
import codegen.instr.loadable.Loadable

class Load(val Rd: Register, val operand: Loadable, cond: Cond = Cond(Condition.AL)) : Instruction(cond) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLoad(this)
    }
}
