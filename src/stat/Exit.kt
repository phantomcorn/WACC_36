package stat

import expr.Expr
import symbols.Int

class Exit(val e: Expr) : Stat() {
    init {
        if (!(e.type is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: Int, actual: " + e.type + ")"
            )
        }
    }
}
