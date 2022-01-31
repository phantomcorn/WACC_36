import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import antlr.*;

import myvisitor.*;

fun main() {

    val input = CharStreams.fromStream(System.`in`);

    val lexer = WACCLexer(input);

    val tokens = CommonTokenStream(lexer);

    val parser = WACCParser(tokens);

    val tree = parser.prog();

    println(tree.toStringTree(parser));

    println("=====");
    val visitor = MyVisitor();
    visitor.visit(tree);
    println("=====");
}