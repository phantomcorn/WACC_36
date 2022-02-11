package expr

import symbols.Null

class PairLiteral() : Literal<Void>(Null) {
    init {
        value = null
    }

    override fun toString(): String {
        return "null"
    }
}
