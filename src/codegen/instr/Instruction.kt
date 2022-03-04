package codegen.instr

abstract class Instruction(val cond: Cond = Cond(Condition.AL), val s: SFlag = SFlag(false)) {
    abstract fun <T> accept(v: InstructionVisitor<T>): T
}
