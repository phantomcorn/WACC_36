package codegen.instr.operand2

import codegen.instr.InstructionVisitor

interface Operand2 {
    fun <T> accept(v: InstructionVisitor<T>): T
}
