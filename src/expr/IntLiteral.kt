package expr

import java.lang.NumberFormatException
import symbols.Int

class IntLiteral(val token: kotlin.String) : Literal<kotlin.Int>(Int) {
    init {
        try {
            if (Int.min < token.toInt() && Int.max > token.toInt()) {
                value = token.toInt()
            } else {
                value = 0
                valid = false
            }
        } catch (e : NumberFormatException) {
            valid = false
        }
    }
}
