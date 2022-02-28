package expr

import stat.AssignRhs
import symbols.Identifier
import symbols.Type

abstract class Expr(val type: Type?) : Identifier(), AssignRhs {
    override fun type() : Type? = type
}
