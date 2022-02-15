package codegen

import ASTVisitor
import instr.Instruction
import symbols.Identifier

abstract class ASTNode: Identifier() {
    abstract fun accept(v: ASTVisitor): List<Instruction>
}
