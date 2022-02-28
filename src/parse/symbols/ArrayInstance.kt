package parse.symbols

import parse.stat.ArrayLiteral

class ArrayInstance(val elementType: Type?) : Array() {
    @get:JvmName("get_dim")
    val dim: kotlin.Int

    @get:JvmName("get_size")
    var size: kotlin.Int = 0
    init {
        var n = 1
        var t = elementType
        while (t is ArrayInstance) {
            t = t.elementType
            n += 1
        }
        dim = n
    }
    override fun toString(): kotlin.String = elementType.toString() + "[]"

    override fun getDim(): kotlin.Int {
        return dim
    }

    override fun getBaseType(): Type? {
        var t = elementType
        while (t is ArrayInstance) {
            t = t.elementType
        }
        return t
    }

    fun setSize(x: ArrayLiteral) {
        size = x.value!!.size
    }

    override fun getSize(): kotlin.Int {
        return size
    }
}
