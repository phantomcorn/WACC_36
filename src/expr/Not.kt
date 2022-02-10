package expr

import symbols.Boolean
import symbols.Identifier

class Not(e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            System.err.println("Type error in not, expected: Boolean, got: " + e.type)
            Identifier.valid = false
        }
    }
}
