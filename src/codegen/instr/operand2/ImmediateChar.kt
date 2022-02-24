package codegen.instr.operand2

import codegen.instr.InstructionVisitor

class ImmediateChar(val value: Char) : Operand2 {
    override fun accept(v: InstructionVisitor): String {
        return v.visitImmediateChar(this)
    }
}
