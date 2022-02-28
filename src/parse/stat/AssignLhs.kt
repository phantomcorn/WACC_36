package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.symbols.Type

interface AssignLhs {
    fun type(): Type?
    fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable>
}
