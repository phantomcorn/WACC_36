package instr

abstract class Instruction {
    open fun accept(v: InstructionVisitor): String {
        return "NOT IMPLEMENTED"
    }
}
