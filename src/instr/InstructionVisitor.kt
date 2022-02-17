package instr
import register.*

import register.Register

interface InstructionVisitor {
    fun visitTest(Rn: Register, op2: Operand2): String

    fun visitTestEquiv(Rn: Register, op2: Operand2): String

    fun visitAnd(Rd: Register, Rn: Register, op2: Operand2): String

    fun visitXor(Rd: Register, Rn: Register, op2: Operand2): String

    fun visitOr(Rd: Register, Rn: Register, op2: Operand2): String
    fun visitAdd(Rd: Register, Rn: Register, op2: Operand2) : String
    fun visitSub(Rd: Register, Rn: Register, op2: Operand2) : String
    fun visitMul(Rd: Register, Rm: Register, Rs: Register) : String
    fun visitBranch(dest: String) : String
    fun visitMove(Rd: Register, op2: Operand2) : String
    fun visitCompare(Rn : Register, op2: Operand2) : String
}
