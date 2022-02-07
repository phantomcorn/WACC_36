package symbols

class PairLiteral(
        e1: Expr,
        e2: Expr,
        t1: Type,
        t2: Type
) : Expr(Pair(t1, t2)) {

    init {
        if (e1.type != t1) {
            valid = false
            System.err.println("Expecting type : " + t1 + " but actual type: " + e1.type)
        } else if (e2.type != t2) {
            valid = false;
            System.err.println("Expecting type : " + t2 + " but actual type: " + e2.type)
        }
    }
}
