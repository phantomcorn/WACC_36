package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction

class Println(val e: Expr) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitPrintlnNode(this)
    }

    override fun toString(): String = "println $e"
}
