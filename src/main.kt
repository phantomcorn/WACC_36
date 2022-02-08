import visitor.Visitor
import org.antlr.v4.runtime.*;

import antlr.*;

fun main() {

    val input = CharStreams.fromStream(System.`in`);

    val lexer = WACCLexer(input);

    val tokens = CommonTokenStream(lexer);

    val parser = WACCParser(tokens);

    val tree = parser.prog();

    println(tree.toStringTree(parser));

    println("=====");
    val visitor = Visitor();
    visitor.visit(tree);
    println("=====");
}