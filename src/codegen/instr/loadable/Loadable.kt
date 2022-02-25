package codegen.instr.loadable

import codegen.instr.InstructionVisitor

interface Loadable {
    fun loadAccept(v: InstructionVisitor): String
}
