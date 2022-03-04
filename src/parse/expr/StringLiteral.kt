package parse.expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.String

class StringLiteral(token: kotlin.String) : Literal<kotlin.String>(String) {

    init {
        value = token.substring(1, token.length - 1)
    }

    override fun toString(): kotlin.String {
        return value!!
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitStringLiteralNode(this)
    }
}
