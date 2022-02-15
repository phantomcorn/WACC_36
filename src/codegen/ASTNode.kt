package codegen

import symbols.Identifier

abstract class ASTNode: Identifier() {
    abstract fun accept(v: ASTVisitor): List<Instruction>
}
