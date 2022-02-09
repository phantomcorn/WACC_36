package expr

import stat.AssignLhs
import symbols.Array
import symbols.Type
import visitor.SymbolTable

class ArrayElem(
    val id: kotlin.String,
    val values: kotlin.Array<Expr>,
    dims: kotlin.Int,
    t: Type?
) : Expr(t), AssignLhs {
    init {
        if (t == null) {
            System.err.println("Array $id not defined")
            valid = false
        } else if (!(t is Array)) {
            System.err.println("Expected Array, got " + t)
            valid = false
        } else if (t.getDim() != dims) {
            System.err.println("Dimension Error")
            valid = false
        } else {

        }
    }
}
