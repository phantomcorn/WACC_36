package expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import symbols.String

class StringLiteral(val token: kotlin.String) : Literal<kotlin.String>(String) {

    init {
        value = token
    }

    override fun toString(): kotlin.String {
        return token
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitStringLiteralNode(token)
    }
}
