package symbols

class Array(val elementType: Type?, val elements: kotlin.Int) : Type() {
    override fun toString(): kotlin.String = elementType.toString() + "[]"

    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        }
        if (other is EmptyArray) {
            return true
        }
        if (!(other is Array)) {
            return false
        }
        return this.toString() == other.toString()
    }

    // a = b -> hashCode(a) = hashCode(b)
    // forall T, [T] = []
    // forall T1, T2, [T1] = [T2], by transitivity
    // forall T1, T2, hashCode(T1) = hashCode(T2)
    // This is not an ideal situation
    override fun hashCode(): kotlin.Int {
        return 0
    }

    fun getDim(): kotlin.Int {
        var n = 1
        var t = elementType
        while (t is Array) {
            t = t.elementType
            n += 1
        }
        return n
    }

    fun getBaseType(): Type? {
        var t = elementType
        while (t is Array) {
            t = t.elementType
        }
        return t
    }
}
