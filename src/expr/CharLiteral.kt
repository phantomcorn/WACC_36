package expr

import symbols.Char
import kotlin.String

class CharLiteral(token: String) : Literal<kotlin.Char>(Char) {

    init {
        if (token.length == 3 && token[1] != '\\' && token[1].code in 0..127) {
            value = token[1]
        } else {
            // kotlin doesn't recognise some escape characters so unicode for these cases
            when (token.substring(1, token.length)) {
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
