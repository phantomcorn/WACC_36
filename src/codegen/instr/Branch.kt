package codegen.instr

class Branch(
    val dest: String,
    cond: Cond = Cond(Condition.AL),
    s: SFlag = SFlag(false)
) : Instruction(cond, s) {
    override fun accept(v: InstructionVisitor): String {
        return v.visitBranch(this)
    }
}
