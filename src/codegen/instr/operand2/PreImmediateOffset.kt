package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class PreImmediateOffset(val r: Register, val value: Immediate) : Operand2, Loadable {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitPreImmediateOffset(this)
    }

    override fun <T> loadAccept(v: InstructionVisitor<T>): T {
        return v.loadPreImmediateOffset(this)
    }
}
