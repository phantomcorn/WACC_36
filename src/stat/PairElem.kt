package stat

import expr.Expr
import symbols.Identifier
import symbols.Pair
import symbols.Type

class PairElem(val text: String, val e: Expr) : Identifier(), AssignLhs, AssignRhs {
    var type: Type? = null
    init {
        if (!(e.type is Pair)) {
            System.err.println("Expected Pair, got $e.type")
            valid = false
        } else {
            if (text == "fst") {
                type = e.type.t1
            } else {
                type = e.type.t2
            }
        }
    }

    override fun type(): Type? = type
}
