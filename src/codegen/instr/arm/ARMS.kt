package codegen.instr.arm

import codegen.instr.SBool

object ARMS {
    fun accept(s: SBool): String{
        return if (s.bool) "S" else ""
    }
}