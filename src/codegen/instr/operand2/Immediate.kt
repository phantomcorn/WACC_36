package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable

class Immediate(val value: Int) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitImmediate(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadImmediate(this)
    }
}
