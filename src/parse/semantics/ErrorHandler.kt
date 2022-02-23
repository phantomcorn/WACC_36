import org.antlr.v4.runtime.ParserRuleContext
import kotlin.system.exitProcess

enum class ErrorType {
    SYNTAX {
        override fun code() = 100
    },
    SEMANTIC {
        override fun code() = 200
    };

    abstract fun code(): Int
}

object ErrorHandler {

    var errorCount: Int = 0
    var line: Int = 0

    fun setContext(ctx: ParserRuleContext) {
        line = ctx.getStart().line
    }

    fun printErr(
        errorType: ErrorType,
        message: String
    ) {
        errorCount++

        when (errorType) {
            ErrorType.SYNTAX -> {
                System.err.println("Syntactic Error at line $line -- $message")
                System.err.println("$errorCount parser error(s) detected, no further compilation attempted.")
                System.err.println("Errors detected during compilation! Exit code " + ErrorType.SYNTAX.code() + " returned.")
                exitProcess(errorType.code())
            }
            ErrorType.SEMANTIC -> {
                System.err.println("Semantic Error at line $line -- $message")
            }
        }
    }
}
