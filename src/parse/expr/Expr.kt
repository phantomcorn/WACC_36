package parse.expr

import codegen.ASTNode
import parse.stat.AssignRhs
import parse.symbols.Type

abstract class Expr(val type: Type?) : ASTNode(), AssignRhs {
    override fun type() : Type? = type
}
