package symbols

class PairLiteral<T1, T2>(
    val1: T1,
    val2: T2,
    t1: Type,
    t2: Type
) : Expr(Pair(t1, t2))
