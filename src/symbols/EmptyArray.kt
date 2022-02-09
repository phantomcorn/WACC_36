package symbols

object EmptyArray : Type() {
    override fun toString(): kotlin.String = "[]"

    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        }
        if (other is EmptyArray || other is Array) {
            return true
        }
        return false
    }

    override fun hashCode(): kotlin.Int {
        return 0
    }
}
