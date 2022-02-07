package symbols

class BooleanLiteral(token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> valid = false
        }
    }
}
