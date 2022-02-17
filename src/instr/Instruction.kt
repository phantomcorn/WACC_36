package instr

abstract class Instruction {
    open fun accept(v: InstructionVisitor): String {
        println("NOT IMPLEMENTED")
    }
}
