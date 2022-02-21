package parse.symbols

class PairInstance(val t1: Type?, val t2: Type?) : Pair() {
    override fun toString(): kotlin.String = "Pair<" + t1 + "," + t2 + ">"

    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        } else if (other is Null || other is TypelessPair) {
            return true
        } else if (!(other is PairInstance)) {
            return false
        } else {
            return this.t1 == other.t1 && this.t2 == other.t2
        }
    }
}
