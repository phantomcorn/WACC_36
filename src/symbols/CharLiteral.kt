package symbols

class CharLiteral(value: kotlin.Char) : Expr(Char) {
    override fun check(): kotlin.Boolean {
        return 0 <= value.toInt() && value.toInt <= 127
    }
}
