package symbols

class IntLiteral(val value: kotlin.Int) : Expr(Int) {
    override fun check(): kotlin.Boolean {
        return Int.min < value && Int.max > value
    }
}
