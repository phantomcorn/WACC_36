package parse.expr

import parse.symbols.Type

abstract class Literal<T>(val t: Type) : Expr(t) {
    protected var value: T? = null
        get() = field
}
