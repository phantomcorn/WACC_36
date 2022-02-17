package instr

class Move (val Rd: Register, val Op2: Operand2) : Instruction {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMove(Rd, Op2)
    }
}
