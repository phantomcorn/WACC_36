package stat

class NewPair(val e1: Expr, val e2: Expr) : Identifier(), assignRhs {
    val type: Pair;
    init {
        type = Pair(e1.type, e2.type)
    }
}
