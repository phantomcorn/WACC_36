package expr

import symbols.Int

class Minus(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Int) {

    init {
        if (e1.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: INT, actual: " + e1.type + ")")
        } else if (e2.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: INT, actual: " + e1.type + ")")
        }
    }

    override fun toString(): String =
        "$e1-$e2"
}
