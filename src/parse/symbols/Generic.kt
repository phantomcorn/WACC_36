package parse.symbols

import kotlin.Int
import kotlin.String

object Generic : Type() {
    override fun getByteSize(): Int {
        throw Exception("called getByteSize on uninstantiated generic type")
    }

    override fun toString(): String = "Generic"
}