package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Type

interface AssignRhs {
    fun type(): Type?
    fun accept(v: ASTVisitor): List<Instruction>
}
