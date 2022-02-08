package symbols

class Pair(val t1: Type?, val t2: Type?) : Type() {
    override fun toString(): kotlin.String = "Pair<" + t1 + "," + t2 + ">"
}
