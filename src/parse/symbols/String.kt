package parse.symbols

object String : Type() {
    override fun toString(): kotlin.String = "String"

    override fun getByteSize(): kotlin.Int = 4
}
