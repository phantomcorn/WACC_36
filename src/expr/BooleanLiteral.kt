package expr

import symbols.Boolean

class BooleanLiteral(token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $token (Expected: Boolean)")
        }
    }
}
