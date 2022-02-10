package expr

import symbols.Array
import symbols.Identifier
import symbols.Int

class Len(e: Expr) : UnaryOp(e, Int) {
    init {
        if (!(e.type is Array)) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: ARRAY, actual ${e.type}")
            Identifier.valid = false
        }
    }
}
