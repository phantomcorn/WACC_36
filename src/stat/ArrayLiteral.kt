package stat

import expr.Expr
import expr.Literal
import symbols.Array
import symbols.Type

class ArrayLiteral(
    values: kotlin.Array<Expr>,
    t: Type?
) : Literal<kotlin.Array<Expr>>(Array(t, values.size)), AssignRhs {

    init {
        for (value in values) {
            if (value.type != t) {
                valid = false
                System.err.println("Expecting type : " + t + " but actual type: " + value.type)
            }
        }
        value = values
    }
}
