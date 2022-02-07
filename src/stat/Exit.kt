package stat

import expr.Expr
import symbols.Int

class Exit(val e: Expr) : Stat() {
    init {
        if (!(e.type is Int)) {
            System.err.println("Exit expected Int, got " + e.type)
            valid = false
        }
    }
}
