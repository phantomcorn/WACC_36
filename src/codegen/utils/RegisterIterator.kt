package codegen.utils

import codegen.instr.register.Register
import codegen.instr.register.GP
import kotlin.collections.ArrayDeque

class RegisterIterator: Iterator<Register> {
    val regs = ArrayDeque<Register>()
    var n = 0

    override fun hasNext(): kotlin.Boolean = true

    override fun next(): Register {
        if (regs.size > 0) {
            return regs.removeFirst()
        } else {
            val result = GP(0, n)
            n += 1
            return result
        }
    }

    fun add(r: Register) {
        regs.addFirst(r)
    }
}
