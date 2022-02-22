package parse.expr

import parse.symbols.Type

abstract class Literal<T>(val t: Type) : Expr(t, 1) {
    protected var value: T? = null
        get() = field
}
