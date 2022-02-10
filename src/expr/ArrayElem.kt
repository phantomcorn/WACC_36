package expr

import stat.AssignLhs
import symbols.Array
import symbols.Identifier
import symbols.Int
import symbols.Type

class ArrayElem(
    val id: kotlin.String,
    val values: kotlin.Array<Expr>,
    dims: kotlin.Int,
    t: Type?
) : Expr(t?.getBaseType()), AssignLhs {
    init {
        if (t == null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Array $id not defined"
            )
            Identifier.valid = false
        } else if (t !is Array) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type (expected: Array, actual $t)"
            )
            Identifier.valid = false
        } else if (t.getDim() != dims) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Dimension Error (expected: ${t.getDim()}, actual: $dims)"
            )
            Identifier.valid = false
        } else {
            for (e in values) {
                if (e.type != Int) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible array element type (expected: Int, actual ${e.type}"
                    )
                    Identifier.valid = false
                }
            }
        }
    }
}
