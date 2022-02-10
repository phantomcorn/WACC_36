enum class ErrorType {
    SYNTAX,
    SEMANTIC
}

class ErrorHandler {

    var errorCount: Int = 0;

    fun printErr(line: Int,
                 index: Int,
                 errorType: ErrorType,
                 exitCode: Int,
                 message: String) {
        println("Errors detected during compilation! Exit code $exitCode returned.")

        when (errorType) {
            ErrorType.SYNTAX -> {
                println("Syntactic Error at $line:$index -- $message")
                println("$errorCount parser error(s) detected, no further compilation attempted.")
            }
            ErrorType.SEMANTIC -> {
                println("Semantic Error at $line:$index -- $message")
            }
            else -> {}
        }
    }
}