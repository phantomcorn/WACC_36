package expr

import symbols.Identifier
import symbols.Int

class Mod(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Int) {

    init {
        if (e1.type != Int) {
            System.err.println("Expected type int but actual type " + e1.type)
            Identifier.valid = false
        } else if (e2.type != Int) {
            System.err.println("Expected type int but actual type " + e2.type)
            Identifier.valid = false
        }
    }
}
