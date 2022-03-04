package codegen.instr

class FuncObj(name: String) {
    var funcName: String = "f_$name"
    var funcBody: MutableList<Instruction> = mutableListOf<Instruction>()
    var user: Boolean = false
}
