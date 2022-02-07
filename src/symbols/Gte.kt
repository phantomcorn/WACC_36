package symbols

class Gte(val e1: Expr, val e2: Expr) : BinaryOp(e1, e2, Boolean) {
    override fun check(): kotlin.Boolean {
        return e1.type == e2.type && (e1.type == Int || e1.type == Char)
    }
}
