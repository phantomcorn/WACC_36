package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class ZeroOffset(val r: Register) : Operand2, Loadable {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitZeroOffset(this)
    }

    override fun <T> loadAccept(v: InstructionVisitor<T>): T {
        return v.loadZeroOffset(this)
    }
}
