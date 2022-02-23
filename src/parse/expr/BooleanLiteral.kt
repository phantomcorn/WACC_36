package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Boolean

class BooleanLiteral(val token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> ErrorHandler.printErr(
                ErrorType.SYNTAX,
                "Boolean value $token is badly formatted"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBooleanLiteralNode(token)
    }
}
