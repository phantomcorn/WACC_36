package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class PreRegisterOffset(val r: Register, val offset: Register) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitPreRegisterOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadPreRegisterOffset(this)
    }
}
