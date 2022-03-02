package codegen.instr.operand2

import codegen.instr.InstructionVisitor
import codegen.instr.loadable.Loadable
import codegen.instr.register.Register

class PreRegisterOffset(val r: Register, val offset: Register) : Operand2, Loadable {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitPreRegisterOffset(this)
    }

    override fun <T> loadAccept(v: InstructionVisitor<T>): T {
        return v.loadPreRegisterOffset(this)
    }
}
