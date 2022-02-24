package parse.expr

import parse.symbols.Type

abstract class Literal<T>(val t: Type) : Expr(t, 1) {
    var value: T? = null
}
