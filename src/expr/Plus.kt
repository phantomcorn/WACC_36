package expr

import symbols.Identifier
import symbols.Int

class Plus(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Int) {

    init {
        if (e1.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: INT, actual: " + e1.type + ")")
            Identifier.valid = false
        } else if (e2.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: INT, actual: " + e1.type + ")")
            Identifier.valid = false
        }
    }

    override fun toString(): String =
        "$e1+$e2"
}
