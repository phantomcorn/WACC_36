package symbols

import kotlin.String

class CharLiteral(value: String) : Expr(Char) {
    var lit: kotlin.Char = '\u0000'

    init {
        if (value.length == 1) {
            value.single().code in 0..127
        } else {
            // kotlin doesn't recognise some escape characters so unicode for these cases
            when (value) {
                "\\0" -> lit = '\u0000'
                "\\b" -> lit = '\b'
                "\\t" -> lit = '\t'
                "\\n" -> lit = '\n'
                "\\f" -> lit = '\u000c'
                "\\r" -> lit = '\r'
                "\\\"" -> lit = '\"'
                "\\'" -> lit = '\''
                "\\\\" -> lit = '\\'
                else -> {
                    valid = false
                }
            }
        }
    }
}
