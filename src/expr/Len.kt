package expr

import symbols.Array
import symbols.Identifier
import symbols.Int

class Len(e: Expr) : UnaryOp(e, Int) {
    init {
        if (!(e.type is Array)) {
            System.err.println("Type error in unary operator len expected: Array, got: " + e.type)
            Identifier.valid = false
        }
    }
}
