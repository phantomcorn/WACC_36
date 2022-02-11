package symbols

class ArrayInstance(val elementType: Type?) : Array() {
    override fun toString(): kotlin.String = elementType.toString() + "[]"

    override fun getDim(): kotlin.Int {
        var n = 1
        var t = elementType
        while (t is ArrayInstance) {
            t = t.elementType
            n += 1
        }
        return n
    }

    override fun getBaseType(): Type? {
        var t = elementType
        while (t is ArrayInstance) {
            t = t.elementType
        }
        return t
    }
}
