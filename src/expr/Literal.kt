package expr

import symbols.Type

abstract class Literal<T>(val t: Type) : Expr(t) {
    protected var value: T? = null
        get() {
            return value
        }
}
