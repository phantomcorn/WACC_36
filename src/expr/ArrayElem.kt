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
            System.err.println("Array $id not defined")
            Identifier.valid = false
        } else if (!(t is Array)) {
            System.err.println("Expected Array, got " + t)
            Identifier.valid = false
        } else if (t.getDim() != dims) {
            System.err.println("Dimension Error")
            Identifier.valid = false
        } else {
            for (e in values) {
                if (!(e.type == Int)) {
                    System.err.println("Expected Int, got " + e.type)
                    Identifier.valid = false
                }
            }
        }
    }
}
