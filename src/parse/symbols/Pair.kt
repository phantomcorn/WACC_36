package parse.symbols

import kotlin.String

abstract class Pair : Type() {
    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        } else if (other is Null || other is TypelessPair) {
            return true
        } else if (!(other is PairInstance)) {
            return false
        } else if (this is Null || this is TypelessPair) {
            return true
        } else {
            return this.toString() == other.toString()
        }
    }

    override fun hashCode(): kotlin.Int {
        return 0
    }

    override fun toArg(): String {
        return "Pair"
    }

    override fun getByteSize(): kotlin.Int = 4
}
