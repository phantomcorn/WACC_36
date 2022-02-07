package symbols

class Or(val e1: Expr, val e2: Expr) : BinaryOp(e1, e2, Boolean) {
    override fun check(): kotlin.Boolean {
        return e1.type == Boolean && e2.type == Boolean
    }
}
