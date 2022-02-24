package codegen.instr.arm

import codegen.instr.Cond
import codegen.instr.Cond.*

object ARMCond {
    fun accept(cond: Cond) : String {
        return when (cond) {
            EQ -> "EQ"
            NE -> "NE"
            HSCS -> "HSCS"
            LOCC -> "LOCC"
            MI -> "MI"
            PL -> "PL"
            VS -> "VS"
            VC -> "VC"
            HI -> "HI"
            LS -> "LS"
            GE -> "GE"
            LT -> "LT"
            GT -> "GT"
            LE -> "LE"
            AL -> ""
        }
    }
}