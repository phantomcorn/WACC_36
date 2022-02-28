package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class PreImmediateOffset(val r: Register, val value: Int) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPreImmediateOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadPreImmediateOffset(this)
    }
}
