package symbols

class Ord(val e: Expr) : UnaryOp(e, Int) {
    override fun check(): kotlin.Boolean {
        return e.type == Char
    }
}
