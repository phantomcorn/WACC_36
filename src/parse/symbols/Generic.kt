package parse.symbols

import kotlin.Int

object Generic : Type() {
    override fun getByteSize(): Int {
        throw Exception("called getByteSize on uninstantiated generic type")
    }
}