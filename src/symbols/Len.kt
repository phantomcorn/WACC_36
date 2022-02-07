package symbols

class Len(val e: Expr) : UnaryOp(e, Int) {
    override fun check(): kotlin.Boolean {
        return e.type == String
    }
}
