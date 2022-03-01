package codegen.instr

class Cond(val cond: Condition) {
    fun accept(v: InstructionVisitor): String{
        return v.visitCond(this)
    }
}

enum class Condition {
    EQ,
    NE,
    HSCS,
    LOCC,
    MI,
    PL,
    VS,
    VC,
    HI,
    LS,
    GE,
    LT,
    GT,
    LE,
    AL
}
