package stat

import expr.Expr
import symbols.Array
import symbols.Identifier
import symbols.Pair

class Free(val e: Expr) : Stat() {
    init {
        if (!(e.type is Pair || e.type is Array)) {
            System.err.println("Free expected Pair or Array, got " + e.type)
            Identifier.valid = false
        }
    }
}
