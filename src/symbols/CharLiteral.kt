package symbols

class CharLiteral(val value: kotlin.Char) : Expr(Char) {
    override fun check(): kotlin.Boolean {
        return value.code in 0..127
    }
}
