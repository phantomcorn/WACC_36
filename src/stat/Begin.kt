package stat

import codegen.ASTVisitor
import instr.Instruction

class Begin(val s: Stat) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "begin $s end"
}
