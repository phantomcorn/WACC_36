package expr

import symbols.Int

class Multi(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Int) {
    init {
        if (e1.type != Int) {
            System.err.println("Expected type int but actual type " + e1.type)
            valid = false
        } else if (e2.type != Int) {
            System.err.println("Expected type int but actual type " + e2.type)
            valid = false
        }
    }
}
