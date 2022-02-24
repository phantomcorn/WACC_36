package codegen.utils

import codegen.instr.register.GP
import codegen.instr.register.Register

class RegisterIterator : Iterator<Register> {
    val regs = ArrayDeque<Register>()
    var n = 0

    override fun hasNext(): kotlin.Boolean = true

    fun peek(): Register {
        val r = next()
        add(r)
        return r
    }

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
