package expr

import symbols.Boolean

class Or(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Boolean) {
    init {
        if (e1.type != Boolean) {
            System.err.println("Expected type int but actual type " + e1.type)
            valid = false
        } else if (e2.type != Boolean) {
            System.err.println("Expected type int but actual type " + e2.type)
            valid = false
        }
    }
}
