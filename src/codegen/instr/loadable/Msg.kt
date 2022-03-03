package codegen.instr.loadable

import codegen.instr.InstructionVisitor

class Msg(val s: String) : Loadable {
    override fun <T> loadAccept(v: InstructionVisitor<T>): T {
        return v.loadMsg(this)
    }
}
