package parse.symbols

object EmptyArray : Array() {
    override fun toString(): kotlin.String = "[]"

    override fun getDim(): kotlin.Int = 0

    override fun getSize(): kotlin.Int = 0
}
