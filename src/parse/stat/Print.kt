package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr

class Print(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintNode(this)
    }

    override fun toString() = "print $e"
}
