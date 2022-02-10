package expr

import symbols.Identifier
import symbols.Int

class UnaryMinus(e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != Int) {
            System.err.println("Type error in unary minus, expected: Int, got: " + e.type)
            Identifier.valid = false
        }
    }
}
