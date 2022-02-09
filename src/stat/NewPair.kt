package stat

import expr.Expr
import symbols.Identifier
import symbols.Pair
import symbols.Type

class NewPair(val e1: Expr, val e2: Expr) : Identifier(), AssignRhs {
    val type: Pair
    init {
        type = symbols.PairInstance(e1.type(), e2.type())
    }

    override fun type(): Type = type
}
