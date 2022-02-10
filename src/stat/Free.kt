package stat

import expr.Expr
import symbols.Array
import symbols.Identifier
import symbols.Pair

class Free(val e: Expr) : Stat() {
    init {
        if (!(e.type is Pair || e.type is Array)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: {Pair, Array}, actual: ${e.type})"
            )
            Identifier.valid = false
        }
    }
}
