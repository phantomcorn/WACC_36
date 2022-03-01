package codegen.instr

class SBool (val bool: Boolean){
    fun accept(v: InstructionVisitor): String {
        return v.visitSBool(this)
    }
}
