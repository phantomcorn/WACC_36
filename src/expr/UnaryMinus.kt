package expr

import symbols.Identifier
import symbols.Int

class UnaryMinus(e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: INT, actual ${e.type})")
            Identifier.valid = false
        }
    }
}
