package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

enum class Shift {
    LSL { override fun toString(): String = "LSL" },
    LSR { override fun toString(): String = "LSR" },
    ASR { override fun toString(): String = "ASR" }
}

class ShiftOffset(val r: Register, val value: Immediate, val shift: Shift) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitShiftOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadShiftOffset(this)
    }
}
