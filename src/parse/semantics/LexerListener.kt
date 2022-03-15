package parse.semantics

object LexerListener : BaseErrorListener {
    override void syntaxError(
        recognizer : Recognizer<*, *>,
        symbol: Object,
        line: int,
        charPos: int,
        msg: String,
        e: RecognitionException) {
        throw ParseCancellationException(line + ": " + msg)
    }
}
