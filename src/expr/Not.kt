package expr

import symbols.Boolean

class Not(e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $e (expected: BOOLEAN, actual ${e.type})")
        }
    }
}
