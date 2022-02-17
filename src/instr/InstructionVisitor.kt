package instr

import register.Register

interface InstructionVisitor {
    fun visitMove(rd: Register, op2: Operand2): String

    fun visitTest(rn: Register, op2: Operand2): String

    fun visitTestEquiv(rn: Register, op2: Operand2): String

    fun visitAnd(rd: Register, rn: Register, op2: Operand2): String

    fun visitXor(rd: Register, rn: Register, op2: Operand2): String

    fun visitOr(rd: Register, rn: Register, op2: Operand2): String
}
