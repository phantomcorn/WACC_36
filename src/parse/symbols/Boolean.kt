package parse.symbols

import kotlin.Boolean

object Boolean : Type() {
    const val False: Boolean = false
    const val True: Boolean = true

    override fun toString(): kotlin.String = "Boolean"

    override fun getByteSize(): kotlin.Int = 1
}
