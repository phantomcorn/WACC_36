package parse.stat

import codegen.ASTVisitor
import parse.expr.Expr
import codegen.instr.Instruction
import parse.symbols.EmptyArray

object EmptyArrayLiteral : Expr(EmptyArray, 1) {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitEmptyArrayLiteralNode(this)
    }
}
