package symbols

class StringLiteral(value: kotlin.String) : Expr(String) {
    override fun check(): kotlin.Boolean = true
}
