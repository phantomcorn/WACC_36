package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr

class Println(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintlnNode(this)
    }

    override fun toString(): String = "println $e"
}
