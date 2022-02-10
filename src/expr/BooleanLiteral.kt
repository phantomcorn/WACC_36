package expr

import symbols.Boolean
import symbols.Identifier

class BooleanLiteral(token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> Identifier.valid = false
        }
    }
}
