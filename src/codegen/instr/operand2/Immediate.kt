package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable

class Immediate(val value: Int) : Operand2, Loadable {

    companion object {
        const val MAX_VALUE = 1024
        const val RETURN_ADDRESS_SIZE = 4
    }

    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitImmediate(this)
    }

    override fun <T> loadAccept(v: InstructionVisitor<T>): T {
        return v.loadImmediate(this)
    }
}
