package codegen.utils

import codegen.instr.register.GP
import codegen.instr.register.Register

class RegisterIterator : Iterator<Register> {
    val regs = ArrayDeque<Register>()
    var n = 4

    companion object {
        val r0 = GP(0)
        val r1 = GP(1)
        val r2 = GP(2)
        val r3 = GP(3)
    }

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
            val result = GP(n)
            n += 1
            return result
        }
    }

    fun add(r: Register) {
        regs.addFirst(r)
    }

    fun add(rs: Set<Register>) {
        regs.addAll(rs)
    }
}
