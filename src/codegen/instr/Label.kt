package codegen.instr

class Label(s: String = "$") : Instruction() {
    val name: String
    init {
        if (s != "$") {
            name = s
        } else {
            name = "L$n"
            n += 1
        }
    }

    override fun <T> accept(v: InstructionVisitor<T>): T {
        return v.visitLabel(this)
    }

    companion object {
        var n = 0
    }
}
