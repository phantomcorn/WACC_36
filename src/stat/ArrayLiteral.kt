package stat

import expr.Expr
import expr.Literal
import symbols.ArrayInstance
import symbols.Identifier
import symbols.Type

class ArrayLiteral(
    values: kotlin.Array<Expr>,
    t: Type?
) : Literal<kotlin.Array<Expr>>(ArrayInstance(t, values.size)), AssignRhs {

    init {
        value = values
        for (value in values) {
            if (value.type != t) {
                Identifier.valid = false
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $value "
                    + "(expected: $t, actual: " + value.type + ")")
            }
        }
    }

    override fun toString(): String {
        var result: String = "["
        if (value == null) {
            return "Null"
        } else {
            if (value!!.size > 0) {
                result = result + value!![0]
                for (i in 1..(value!!.size - 1)) {
                    result = ", " + value!![i]
                }
            }
        }
        return result + "]"
    }
}
