package expr

import symbols.Boolean
import symbols.Identifier

class Not(e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: BOOLEAN, actual ${e.type})")
            Identifier.valid = false
        }
    }
}
