package codegen.instr.operand2

import codegen.instr.InstructionVisitor

class Immediate(val value : Int) : Operand2 {
    override fun accept(v: InstructionVisitor): String {
        return v.visitImmediate(this)
    }
}