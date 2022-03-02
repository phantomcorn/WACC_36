package codegen.instr.loadable

import codegen.instr.InstructionVisitor

interface Loadable {
    fun <T> loadAccept(v: InstructionVisitor<T>): T
}
