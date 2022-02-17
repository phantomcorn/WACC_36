package instr
import register.*

interface InstructionVisitor {
    fun visitAdd(Rd: Register, Rn: Register, operand2: Operand2) : String
    fun visitSub(Rd: Register, Rn: Register, operand2: Operand2) : String
    fun visitMul(Rd: Register, Rm: Register, Rs: Register) : String
    fun visitMove(Rd: Register, operand2: Operand2): String
}
