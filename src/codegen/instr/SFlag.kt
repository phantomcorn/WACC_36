package codegen.instr

class SFlag (val bool: Boolean){
    fun accept(v: InstructionVisitor): String {
        return v.visitSBool(this)
    }
}
