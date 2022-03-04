package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Char

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
                    ErrorHandler.printErr(ErrorType.SYNTAX, "Char value $token is badly formatted ")
                }
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitCharLiteralNode(this)
    }
}
