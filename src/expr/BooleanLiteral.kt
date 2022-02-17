package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Boolean

class BooleanLiteral(val token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $token (Expected: Boolean)")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBooleanLiteralNode(token)
    }
}
