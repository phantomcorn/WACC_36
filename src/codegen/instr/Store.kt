package codegen.instr

import codegen.instr.register.Register
import codegen.instr.loadable.Loadable

class Store(val Rd: Register, val operand: Loadable, cond: Cond = Cond.AL) : Instruction(cond) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitStore(this)
    }
}
