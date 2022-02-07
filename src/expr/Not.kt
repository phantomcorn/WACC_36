package expr

import symbols.Boolean

class Not(e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            System.err.println("Type error in not, expected: Boolean, got: " + e.type)
            valid = false
        }
    }
}
