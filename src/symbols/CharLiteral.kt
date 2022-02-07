package symbols

import kotlin.String

class CharLiteral(private val value: String) : Expr(Char) {
    override fun check(): kotlin.Boolean {
        return if (value.length == 1) {
            value.single().code in 0..127
        } else {
            return when(value){
                "\\0", "\\b", "\\t", "\\n", "\\f", "\\r", "\\\"", "\\'", "\\\\" -> true
                else -> false
            }
        }
    }
}
