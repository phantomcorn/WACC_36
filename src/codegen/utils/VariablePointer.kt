package codegen.utils

object VariablePointer {

    private var currentOffset = 0

    fun decrement(offset : Int) {
        currentOffset -= offset
    }

    fun getCurrentOffset(): Int {
        return currentOffset
    }
}