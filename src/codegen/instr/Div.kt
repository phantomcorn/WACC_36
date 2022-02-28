package codegen.instr

object Div : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitDiv(this)
    }
}
