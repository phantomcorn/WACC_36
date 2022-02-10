import org.antlr.v4.runtime.ParserRuleContext

enum class ErrorType {
    SYNTAX {override fun code() = 100},
    SEMANTIC {override fun code() = 200};
    abstract fun code(): Int
}

object ErrorHandler {

    var errorCount: Int = 0
    var line: Int = 0

    fun setContext(ctx: ParserRuleContext) {
        line = ctx.getStart().getLine()
    }

    fun printErr(
        errorType: ErrorType,
        message: String
    ) {
        println("Errors detected during compilation! Exit code " + errorType.code() +" returned.")

        when (errorType) {
            ErrorType.SYNTAX -> {
                println("Syntactic Error at line $line -- $message")
                println("$errorCount parser error(s) detected, no further compilation attempted.")
            }
            ErrorType.SEMANTIC -> {
                println("Semantic Error at $line -- $message")
            }
        }
    }
}
