package expr

import codegen.ASTNode
import instr.Instruction
import symbols.Boolean

class BooleanLiteral(token: kotlin.String) : Literal<kotlin.Boolean>(Boolean) {
    init {
        when (token) {
            "true" -> value = true
            "false" -> value = false
            else -> ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $token (Expected: Boolean)")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {}
}
