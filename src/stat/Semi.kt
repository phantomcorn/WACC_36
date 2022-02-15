package stat

import codegen.ASTVisitor
import instr.Instruction

class Semi(val s1: Stat, val s2: Stat) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "$s1 ; $s2"
}
