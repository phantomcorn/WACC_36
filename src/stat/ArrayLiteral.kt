package stat

import expr.Expr
import expr.Literal
import symbols.ArrayInstance
import symbols.Type

class ArrayLiteral(
    values: kotlin.Array<Expr>,
    t: Type?
) : Literal<kotlin.Array<Expr>>(ArrayInstance(t, values.size)), AssignRhs {

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
