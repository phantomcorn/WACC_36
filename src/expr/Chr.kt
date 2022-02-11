package expr

import symbols.Char
import symbols.Int

class Chr(e: Expr) : UnaryOp(e, Char) {
    init {
        if (e.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: INT, actual ${e.type})")
        }
    }
}
