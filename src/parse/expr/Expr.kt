package parse.expr

import codegen.ASTNode
import parse.stat.AssignRhs
import parse.symbols.Type

abstract class Expr(val type: Type?, val weight: kotlin.Int) : ASTNode(), AssignRhs {
    override fun type() : Type? = type
}
