package expr

import symbols.String

class StringLiteral(val token: kotlin.String) : Literal<kotlin.String>(String) {

    init {
        value = token
    }

    override fun toString(): kotlin.String {
        return token
    }
}
