package codegen.instr

class BranchWithLink(
    private val dest: String,
    cond: Cond = Cond.AL,
    s : Boolean = false
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitBranchWithLink(this)
    }
}
