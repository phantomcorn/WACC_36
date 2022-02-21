package parse.symbols

import kotlin.Int

object Int : Type() {
    const val min: Int = Int.MIN_VALUE
    const val max: Int = Int.MAX_VALUE

    override fun toString(): kotlin.String = "Int"
}
