package expr

import symbols.Boolean
import symbols.Identifier

class And(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Boolean) {
    init {
        if (e1.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: BOOL, actual: " + e1.type + ")")
            Identifier.valid = false
        } else if (e2.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: BOOL, actual: " + e2.type + ")")
            Identifier.valid = false
        }
    }

    override fun toString() : String =
        "$e1&&$e2"
}
