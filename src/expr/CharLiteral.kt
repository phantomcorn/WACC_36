package expr

import codegen.ASTVisitor
import instr.Identifier
import symbols.Char
import kotlin.String

class CharLiteral(val token: String) : Literal<kotlin.Char>(Char) {

    override fun toString(): String {
        return token
    }

    init {
        if (token.length == 3 && token[1] != '\\' && token[1].code in 0..127) {
            value = token[1]
        } else {
            // kotlin doesn't recognise some escape characters so unicode for these cases
            when (token.substring(1, token.length - 1)) {
                "\\0" -> value = '\u0000'
                "\\b" -> value = '\b'
                "\\t" -> value = '\t'
                "\\n" -> value = '\n'
                "\\f" -> value = '\u000c'
                "\\r" -> value = '\r'
                "\\\"" -> value = '\"'
                "\\'" -> value = '\''
                "\\\\" -> value = '\\'
                else -> {
                    ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $token (Expected: Char)")
                }
            }
        }
    }

    override fun ASTNode(v: ASTVisitor): List<Instruction> {}
}
