package codegen.instr

class SFlag(val bool: Boolean) {
    fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitSBool(this)
    }
}
