package codegen.utils

import codegen.instr.operand2.register.Register
import codegen.instr.InstructionVisitor
import codegen.instr.Instruction
import codegen.instr.Push
import codegen.instr.Pop

object SaveRegisters {
    fun formatRegList(reglist: List<Register>, v: InstructionVisitor): String {
        val sb = StringBuilder()
        sb.append("{")
        if (reglist.size > 0) {
            sb.append(reglist[0].accept(v))
        }
        for (i in 2..(reglist.size-1)) {
            sb.append(",")
            sb.append(reglist[i].accept(v))
        }
        sb.append("}")
        return sb.toString()
    }

    fun saveRegisters(regsInUse: List<Register>): List<Instruction> {
        return listOf(Push(regsInUse))
    }

    fun restoreRegisters(regsInUse: List<Register>): List<Instruction> {
        return listOf(Pop(regsInUse.reversed()))
    }
}
