package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class ImmediateOffset(val r: Register, val value: Int) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitImmediateOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadImmediateOffset(this)
    }
}
