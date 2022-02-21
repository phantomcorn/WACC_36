package parse.stat

import parse.expr.Expr
import parse.symbols.Identifier

class ArgList(val values: Array<Expr>) : Identifier() {
    override fun toString(): String {
        var result = "("
        if (values.size > 0) {
            result = result + values[0]
            for (i in 1..(values.size - 1)) {
                result = result + ", " + values[i]
            }
        }
        return result + ")"
    }
}
