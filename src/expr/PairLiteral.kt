package expr

import symbols.Identifier
import symbols.Null

class PairLiteral() : Literal<Void>(Null) {
    init {
        value = null
    }
}
