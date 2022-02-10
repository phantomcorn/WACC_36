package expr

import symbols.Char
import symbols.Identifier
import symbols.Int

class Ord(e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != Char) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: CHAR, actual ${e.type})")
            Identifier.valid = false
        }
    }
}
