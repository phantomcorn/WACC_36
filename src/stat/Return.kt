package stat

import ErrorHandler
import ErrorType
import expr.Expr
import symbols.Identifier
import symbols.Type

class Return(val e: Expr, t: Type?) : Stat() {
    init {
        if (e.type != t) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $this (expected: $t, actual: ${e.type}"
            )
            Identifier.valid = false
        }
    }
}
