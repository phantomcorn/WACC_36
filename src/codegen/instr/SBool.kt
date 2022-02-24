package codegen.instr

class SBool (private val bool: Boolean){
    override fun toString(): String {
        return if (bool) "S" else ""
    }
}