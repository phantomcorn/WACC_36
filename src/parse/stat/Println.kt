package parse.stat

import codegen.ASTVisitor
import parse.expr.Expr
import codegen.instr.Instruction

class Println(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintlnNode(this)
    }

    override fun toString(): String = "println $e"
}
