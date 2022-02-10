package expr

import symbols.Identifier
import symbols.Int

class Neg(e1: Expr) : UnaryOp(e1, Int) {

    init {
        if (e1.type != Int) {
            System.err.println("Expected type int but actual type " + e1.type)
            Identifier.valid = false
        }
    }
}
