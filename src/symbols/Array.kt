package symbols

abstract class Array : Type() {
    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        } else if (other is EmptyArray) {
            return true
        } else if (!(other is Array)) {
            return false
        } else if (this is EmptyArray) {
            return true
        } else if (getDim() != other.getDim()) {
           return false
        } else {
            return getBaseType() == other.getBaseType()
        }
    }

    // a = b -> hashCode(a) = hashCode(b)
    // forall T, [T] = []
    // forall T1, T2, [T1] = [T2], by transitivity
    // forall T1, T2, hashCode(T1) = hashCode(T2)
    // This is not an ideal situation
    override fun hashCode(): kotlin.Int {
        return 0
    }

    abstract fun getDim(): kotlin.Int
}
