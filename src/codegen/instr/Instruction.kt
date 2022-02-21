package codegen.instr

abstract class Instruction (val cond: Cond = Cond.AL, val s: Boolean = false){
    open fun accept(v: InstructionVisitor): String {
        return "NOT IMPLEMENTED"
    }
}
