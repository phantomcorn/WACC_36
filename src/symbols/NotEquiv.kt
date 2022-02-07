package symbols

class NotEquiv(val e1: Expr, val e2: Expr) : BinaryOp(e1, e2, Boolean) {
    override fun check(): kotlin.Boolean {
        return (e1.type == e2.type) &&
               (e1.type == Int ||
                e1.type == Char ||
                e1.type == Boolean ||
                e1.type == String ||
                e1.type is Array ||
                e1.type is Pair)
    }
}
