package stat

import codegen.ASTVisitor
import instr.Instruction
import symbols.Type
import visitor.SymbolTable

class Begin(val s: Stat, val st: SymbolTable<Type>) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBeginNode(s)
    }

    override fun toString(): String = "begin $s end"
}
