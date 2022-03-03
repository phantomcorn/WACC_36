package codegen.instr

class BranchWithLink(
    val dest: String,
    cond: Cond = Cond(Condition.AL),
    s: SFlag = SFlag(false)
) : Instruction(cond, s) {
    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitBranchWithLink(this)
    }
}
