package instr

class And(val Rd: Register, val Rn: Register, val Op2: Operand2) : Instruction {
    override fun accept(v: InstructionVisitor): String {
        return v.visitAnd(Rd, Rn, Op2)
    }
}
