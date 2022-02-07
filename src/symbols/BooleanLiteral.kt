package symbols

class BooleanLiteral(value: kotlin.Boolean) : Expr(Boolean) {
    override fun check(): kotlin.Boolean = true
}
