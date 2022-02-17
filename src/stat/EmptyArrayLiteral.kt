package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.EmptyArray

object EmptyArrayLiteral : Expr(EmptyArray) {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitEmptyArrayLiteralNode()
    }
}
