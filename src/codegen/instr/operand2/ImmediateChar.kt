package codegen.instr.operand2

import codegen.instr.InstructionVisitor

class ImmediateChar(val value: Char) : Operand2 {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitImmediateChar(this)
    }
}
