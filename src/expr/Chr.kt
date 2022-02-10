package expr

import symbols.Char
import symbols.Identifier
import symbols.Int

class Chr(e: Expr) : UnaryOp(e, Char) {
    init {
        if (e.type != Int) {
            System.err.println("Type error in unary operator len expected: Int, got: " + e.type)
            Identifier.valid = false
        }
    }
}
