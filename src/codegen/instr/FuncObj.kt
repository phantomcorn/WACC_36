package codegen.instr

class FuncObj(name: String) {
    val funcName: String = "f_$name"
    val funcBody: MutableList<Instruction> = mutableListOf<Instruction>()
}
