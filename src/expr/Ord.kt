package expr

import symbols.Char
import symbols.Int

class Ord(e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != Char) {
            System.err.println("Type error in unary operator ord expected: Char, got: " + e.type)
            valid = false
        }
    }
}
