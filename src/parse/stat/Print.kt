package parse.stat

import codegen.ASTVisitor
import parse.expr.Expr
import codegen.instr.Instruction

class Print(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintNode(this)
    }

    override fun toString() = "print $e"
}
