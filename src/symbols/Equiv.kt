package symbols


class Equiv(val e1: Expr, val e2: Expr) : BinaryOp(e1, e2, Boolean) {

    init {
        if (e1.type != e2.type) {
            System.err.println("Mismatched expression types " + e1.type + " and " + e2.type)
            valid = false
        } else if (e1.type == Int ||
                   e1.type == Char ||
                   e1.type == Boolean ||
                   e1.type == String ||
                   e1.type is Array ||
                   e1.type is Pair) {
            System.err.println("Invalid expression type " + e1.type)
            valid = false
        }
    }
}
