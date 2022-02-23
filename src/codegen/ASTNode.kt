package codegen

import codegen.instr.Instruction
import parse.symbols.Identifier

abstract class ASTNode : Identifier() {
    abstract fun accept(v: ASTVisitor): List<Instruction>
}
