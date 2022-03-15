package parse.symbols

abstract class Type : Identifier() {
    override fun equals(other: Any?): kotlin.Boolean {
        if (other == null) {
            return false
        }
        if (!(other is Type)) {
            return false
        }
        return this.toString() == other.toString()
    }

    override fun hashCode(): kotlin.Int {
        return this.toString().hashCode()
    }

    open fun toArg() : kotlin.String {
        return toString()
    }

    open fun getBaseType(): Type? {
        return this
    }

    abstract fun getByteSize(): kotlin.Int
}
