package symbols

class UnaryMinus(val e: Expr) : UnaryOp(e, Int) {
    override fun check(): kotlin.Boolean {
        return e.type == Int
    }
}
