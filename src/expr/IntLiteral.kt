package expr

import symbols.Identifier
import symbols.Int
import java.lang.NumberFormatException

class IntLiteral(val token: kotlin.String) : Literal<kotlin.Int>(Int) {

    init {
        try {
            if (Int.min <= token.toInt() && Int.max >= token.toInt()) {
                value = token.toInt()
            } else {
                value = 0
                ErrorHandler.printErr(
                    ErrorType.SYNTAX,
                    "Integer value $token is badly formatted (either it has a badly defined sign or it is too large for a 32-bit integer)"
                )
                Identifier.valid = false
            }
        } catch (e: NumberFormatException) {
            value = 0
            ErrorHandler.printErr(
                ErrorType.SYNTAX,
                "Integer value $token is badly formatted (either it has a badly defined sign or it is too large for a 32-bit integer)"
            )
            Identifier.valid = false
        }
    }

    override fun toString(): String {
        return "${token.toInt()}"
    }
}
