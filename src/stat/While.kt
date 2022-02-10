package stat

import expr.Expr
import symbols.Boolean
import symbols.Identifier

class While(val e: Expr, val s: Stat) : Stat() {
    init {
        if (e.type !is Boolean) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: BOOL, actual: ${e.type})"
            )
            Identifier.valid = false
        }
    }

    override fun toString(): String = "while $e do $s done"
}
