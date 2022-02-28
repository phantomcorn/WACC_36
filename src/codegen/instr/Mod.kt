package codegen.instr

object Mod : Instruction() {
    override fun accept(v: InstructionVisitor): String {
        return v.visitMod(this)
    }
}
