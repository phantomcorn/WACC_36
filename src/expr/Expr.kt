package expr

import codegen.ASTNode
import stat.AssignRhs
import symbols.Identifier
import symbols.Type

abstract class Expr(val type: Type?) : ASTNode(), AssignRhs {
    override fun type() : Type? = type
}
