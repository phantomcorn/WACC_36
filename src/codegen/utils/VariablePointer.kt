package codegen.utils

object VariablePointer {

    private var pointerStack = ArrayDeque<Int>()
    private var currScopeOffset = 0
    private var totalOffset = 0

    init {
        push()
    }

    fun decrement(offset: Int) {
        currScopeOffset -= offset
        totalOffset -= offset
    }

    fun getCurrentOffset(): Int {
        return currScopeOffset
    }

    fun push() {
        pointerStack.addFirst(0)
        //set currentOffset to 0
        currScopeOffset = pointerStack.first()
    }

    fun pop(): Int {
        val res = pointerStack.removeFirst()
        totalOffset -= res
        currScopeOffset = pointerStack.first()
        return res
    }

    fun level(): Int {
        return pointerStack.size - 1
    }
}
