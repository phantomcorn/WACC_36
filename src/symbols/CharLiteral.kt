package symbols

import kotlin.String

class CharLiteral(value: String) : Expr(Char) {
    var lit : kotlin.Char = '\u0000'
    init {
        if (value.length == 1) {
            value.single().code in 0..127
        } else {
            when(value){
                "\\0", "\\b", "\\t", "\\n", "\\f", "\\r", "\\\"", "\\'", "\\\\" -> lit = ' '
                else -> {
                    valid = false;
                }
            }
        }
    }
}
