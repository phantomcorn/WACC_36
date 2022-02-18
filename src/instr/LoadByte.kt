package instr

import register.Register

class LoadByte(val Rd: Register, val a_mode2: A_Mode2, cond: Cond = Cond.AL) : Instruction(cond) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitLoadByte(this)
    }
}
