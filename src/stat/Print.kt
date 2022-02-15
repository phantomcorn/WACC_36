package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction

class Print(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString() = "print $e"
}
