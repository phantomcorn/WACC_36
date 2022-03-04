package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.semantics.SymbolTable
import parse.symbols.Type

class Begin(val s: Stat, val st: SymbolTable<Type>) : Stat() {
    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBeginNode(this)
    }

    override fun toString(): String = "begin $s end"
}
