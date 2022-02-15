package stat

import codegen.ASTVisitor
import instr.Instruction

object Skip : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "skip"
}
