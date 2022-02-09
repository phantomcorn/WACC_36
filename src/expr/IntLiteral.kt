package expr

import symbols.Int
import java.lang.NumberFormatException

class IntLiteral(val token: kotlin.String) : Literal<kotlin.Int>(Int) {

    override fun toString(): String {
        return "${token.toInt()}"
    }

    init {
        try {
            if (Int.min <= token.toInt() && Int.max >= token.toInt()) {
                value = token.toInt()
            } else {
                value = 0
                valid = false
            }
        } catch (e: NumberFormatException) {
            value = 0
            valid = false
        }
    }
}
