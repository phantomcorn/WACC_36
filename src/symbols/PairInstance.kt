package symbols

class PairInstance(val t1: Type?, val t2: Type?) : Pair() {
    override fun toString(): kotlin.String = "Pair<" + t1 + "," + t2 + ">"
}
