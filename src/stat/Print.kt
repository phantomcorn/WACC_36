package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction

class Print(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintNode(this)
    }

    override fun toString() = "print $e"
}
