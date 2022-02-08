package symbols

class Array(val elementType: Type?, val elements: kotlin.Int) : Type() {
    override fun toString(): kotlin.String = elementType.toString() + "[]"
}
