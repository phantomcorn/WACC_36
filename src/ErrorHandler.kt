enum class ErrorType {
    SYNTAX,
    SEMANTIC
}

object ErrorHandler {

    var errorCount: Int = 0
    var line: Int = 0
    var index: Int = 0
    var exitCode: Int = 0

    fun printErr(
        errorType: ErrorType,
        message: String
    ) {
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