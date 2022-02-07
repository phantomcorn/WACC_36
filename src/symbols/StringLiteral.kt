package symbols

class StringLiteral(token: kotlin.String) : Literal<kotlin.String>(String) {
    init {
        value = token
    }
}
