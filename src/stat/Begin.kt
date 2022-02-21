package stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import symbols.Type
import visitor.SymbolTable

class Begin(val s: Stat, val st: SymbolTable<Type>) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBeginNode(this)
    }

    override fun toString(): String = "begin $s end"
}
