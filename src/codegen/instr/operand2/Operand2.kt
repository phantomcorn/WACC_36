package codegen.instr.operand2

import codegen.instr.InstructionVisitor

interface Operand2 {
    open fun accept(v : InstructionVisitor): String
}