package codegen.instr

class SBool (val bool: Boolean) : Instruction(){
    override fun accept(v: InstructionVisitor): String {
        return v.visitSBool(this)
    }
}
