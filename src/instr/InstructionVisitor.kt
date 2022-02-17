package instr

interface InstructionVisitor {
    fun visitMove(Register, Operand2): String
}
