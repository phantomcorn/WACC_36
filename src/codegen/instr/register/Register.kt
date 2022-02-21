package codegen.instr.register

import codegen.instr.InstructionVisitor

abstract class Register (val value: Int){
    abstract fun accept(v : InstructionVisitor) : String
}