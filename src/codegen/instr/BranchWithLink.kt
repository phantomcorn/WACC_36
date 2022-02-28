package codegen.instr

class BranchWithLink(
    val dest: String,
    cond: Cond = Cond.AL,
    s: SBool = SBool(false)
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitBranchWithLink(this)
    }
}
