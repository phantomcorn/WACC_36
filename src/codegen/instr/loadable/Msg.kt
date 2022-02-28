package codegen.instr.loadable

import codegen.instr.InstructionVisitor

class Msg(val s: String): Loadable {
    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadMsg(this)
    }
}
