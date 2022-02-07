package expr

import symbols.Char
import kotlin.String

class CharLiteral(token: String) : Literal<kotlin.Char>(Char) {

    init {
        if (token.length == 1) {
            token.single().code in 0..127
            value = token.single()
        } else {
            // kotlin doesn't recognise some escape characters so unicode for these cases
            when (token) {
                "\\0" -> value = '\u0000'
                "\\b" -> value = '\b'
                "\\t" -> value = '\t'
                "\\n" -> value = '\n'
                "\\f" -> value = '\u000c'
                "\\r" -> value = '\r'
                "\\\"" -> value = '\"'
                "\\'" -> value = '\''
                "\\\\" -> value = '\\'
                else -> {
                    valid = false
                }
            }
        }
    }
}
