import antlr.*
import org.antlr.v4.runtime.*
import visitor.Visitor
import symbols.Identifier
import kotlin.system.exitProcess

fun main() {

    //println("-- Compiling...")

    val input = CharStreams.fromStream(System.`in`)

    val lexer = WACCLexer(input)

    val tokens = CommonTokenStream(lexer)

    val parser = WACCParser(tokens)

    val tree = parser.prog()
    System.err.println(tree.toStringTree(parser))

    if (parser.getNumberOfSyntaxErrors() > 0) {
        ErrorHandler.printErr(
            ErrorType.SYNTAX,
            ""
        )
    }

    val visitor = Visitor()
    visitor.visit(tree)

    if (ErrorHandler.errorCount > 0) {
        System.err.println("Errors detected during compilation! Exit code " + ErrorType.SEMANTIC.code() + " returned.")
        exitProcess(ErrorType.SEMANTIC.code())
    }
}
