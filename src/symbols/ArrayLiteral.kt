package symbols

class ArrayLiteral<T>(
    values: kotlin.Array<T>,
    t: Type
) : Expr(Array(t, values.size))
