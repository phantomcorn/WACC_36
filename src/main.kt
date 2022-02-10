import antlr.*
import org.antlr.v4.runtime.*
import visitor.Visitor

fun main() {

    val input = CharStreams.fromStream(System.`in`)

    val lexer = WACCLexer(input)

    val tokens = CommonTokenStream(lexer)

    val parser = WACCParser(tokens)

    val tree = parser.prog()
    //System.err.println(tree.toStringTree(parser))

    if (parser.getNumberOfSyntaxErrors() > 0) {
        println("Syntax Error")
        return
    }

    val visitor = Visitor()
    visitor.visit(tree)
}
