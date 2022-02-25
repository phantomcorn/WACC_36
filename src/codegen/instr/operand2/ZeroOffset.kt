package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class ZeroOffset(val r: Register) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitZeroOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadZeroOffset(this)
    }
}
