package codegen.instr

object Mod : Instruction() {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitMod(this)
    }
}
