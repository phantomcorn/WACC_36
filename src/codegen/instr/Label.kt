package codegen.instr

class Label(s: String = "$") : Instruction() {
    val name: String
    init {
        if (s != "$") {
            name = s
        } else {
            name = "L{n}"
            n += 1
        }
    }

    override fun accept(v: InstructionVisitor): String {
        return v.visitLabel(this)
    }

    companion object {
        var n = 0
    }
}
