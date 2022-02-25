package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class RegisterOffset(val r: Register, val offset: Register) : Operand2, Loadable {
    override fun accept(v: InstructionVisitor): String {
        return v.visitRegisterOffset(this)
    }

    override fun loadAccept(v: InstructionVisitor): String {
        return v.loadRegisterOffset(this)
    }
}
