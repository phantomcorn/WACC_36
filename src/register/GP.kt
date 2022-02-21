package register

import instr.InstructionVisitor

class GP (value : Int, val id : Int): Register(value){
    override fun accept(v : InstructionVisitor) : String {
        return v.visitGPRegister(this)
    }
}