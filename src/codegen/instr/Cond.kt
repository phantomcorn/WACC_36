package codegen.instr

class Cond(val cond: Condition) {
    fun <T> accept(v: InstructionVisitor<T>): T {
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
    AL,
    CS
}
