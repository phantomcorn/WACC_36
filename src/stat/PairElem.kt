package stat

import expr.Expr
import symbols.Type

class PairElem(val text: String, val e: Expr) : AssignLhs, AssignRhs {
    override fun type(): Type? {
        return e.type
    }
}
