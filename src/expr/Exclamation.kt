package expr

import symbols.Boolean

class Exclamation(e1: Expr) : UnaryOp(e1, Boolean) {

    init {
        if (e1.type != Boolean) {
            System.err.println("Expected type boolean but actual type " + e1.type)
            valid = false
        }
    }
}