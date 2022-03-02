package codegen.instr

abstract class Instruction(val cond: Cond = Cond(Condition.AL), val s: SFlag = SFlag(false)) {
    open fun accept(v: InstructionVisitor): String {
        return "NOT IMPLEMENTED"
    }
}
