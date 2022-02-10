package stat

import expr.Expr
import symbols.Boolean
import symbols.Identifier

class If(val e: Expr, val s1: Stat, val s2: Stat) : Stat() {
    init {
        if (!(e.type is Boolean)) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $e (expected: BOOL, actual: ${e.type}")
            Identifier.valid = false
        }
    }
}
