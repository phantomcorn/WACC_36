package instr
import register.*

import register.Register

interface InstructionVisitor {
    fun visitTest(rn: Register, op2: Operand2): String

    fun visitTestEquiv(rn: Register, op2: Operand2): String

    fun visitAnd(rd: Register, rn: Register, op2: Operand2): String

    fun visitXor(rd: Register, rn: Register, op2: Operand2): String

    fun visitOr(rd: Register, rn: Register, op2: Operand2): String
    fun visitAdd(Rd: Register, Rn: Register, operand2: Operand2) : String
    fun visitSub(Rd: Register, Rn: Register, operand2: Operand2) : String
    fun visitMul(Rd: Register, Rm: Register, Rs: Register) : String
    fun visitMove(Rd: Register, operand2: Operand2): String
}
