package register

import instr.InstructionVisitor

abstract class Register (val value: Int){
    abstract fun accept(v : InstructionVisitor) : String
}