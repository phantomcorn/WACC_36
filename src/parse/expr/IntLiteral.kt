package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Int

class IntLiteral(val token: kotlin.String) : Literal<kotlin.Int>(Int) {

    init {
        try {
            if (Int.min <= token.toInt() && Int.max >= token.toInt()) {
                value = token.toInt()
            } else {
                value = 0
                ErrorHandler.printErr(
                    ErrorType.SYNTAX,
                    "Integer value $token is badly formatted (either it has a badly defined sign or it is too large for a 32-bit integer)"
                )
            }
        } catch (e: NumberFormatException) {
            value = 0
            ErrorHandler.printErr(
                ErrorType.SYNTAX,
                "Integer value $token is badly formatted (either it has a badly defined sign or it is too large for a 32-bit integer)"
            )
        }
    }

    override fun toString(): String {
        return "${token.toInt()}"
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitIntLiteralNode(token)
    }
}
