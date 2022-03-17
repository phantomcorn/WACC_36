package parse.symbols

import kotlin.Int
import kotlin.String

object TypelessArray : Array() {
    override fun getDim(): Int = 0
    override fun getSize(): Int = 0

    override fun toString(): String = "Array"
}