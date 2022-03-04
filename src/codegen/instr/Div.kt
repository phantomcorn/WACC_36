package codegen.instr

object Div : Instruction() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitDiv(this)
    }
}
