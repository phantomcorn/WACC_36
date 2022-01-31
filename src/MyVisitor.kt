package myvisitor

import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode

class MyVisitor : WACCParserBaseVisitor<Void>() {

    override fun visitProg(ctx: WACCParser.ProgContext): Void? {
        println("Program visit")
        return null;
    }

    override fun visitFunc(ctx: WACCParser.FuncContext): Void? {
        println("There's a function in this tree");
        return null;
    }
}