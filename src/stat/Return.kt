package stat

import expr.Expr
import symbols.Type

class Return(val e: Expr, t: Type?) : Stat() {
    init {
        if (e.type != t) {
            System.err.println("Return expected type " + t + ", got " + e.type)
            valid = false
        }
    }
}
