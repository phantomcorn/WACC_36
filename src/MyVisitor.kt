package myvisitor

import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode

class MyVisitor : WACCParserBaseVisitor<Void>() {

    override fun visitStat(ctx: WACCParser.StatContext): Void? {
        println("Stat visit")
        return visitChildren(ctx);
    }

    override fun visitAssign_lhs(ctx: WACCParser.Assign_lhsContext): Void? {
        println("Assign lhs visit")
        return visitChildren(ctx);
    }

        
    override fun visitAssign_rhs(ctx: WACCParser.Assign_rhsContext): Void? {
        println("Assign rhs visit")
        return visitChildren(ctx);
    }

    override fun visitArray_liter(ctx: WACCParser.Array_literContext): Void? {
        println("Array literal visit")
        return visitChildren(ctx);
    }

    override fun visitBase_type(ctx: WACCParser.Base_typeContext): Void? {
        println("Base type visit")
        return visitChildren(ctx);
    }

    override fun visitPair_elem_type(ctx: WACCParser.Pair_elem_typeContext): Void? {
        println("Pair elem type visit")
        return visitChildren(ctx);
    }

    override fun visitPair_type(ctx: WACCParser.Pair_typeContext): Void? {
        println("Pair type visit")
        return visitChildren(ctx);
    }

    override fun visitType(ctx: WACCParser.TypeContext): Void? {
        println("Type visit")
        return visitChildren(ctx);
    }

    override fun visitFunc(ctx: WACCParser.FuncContext): Void? {
        println("Func visit")
        return visitChildren(ctx);
    }

    override fun visitArg_list(ctx: WACCParser.Arg_listContext): Void? {
        println("Arg list visit")
        return visitChildren(ctx);
    }

    override fun visitParam(ctx: WACCParser.ParamContext): Void? {
        println("Param visit")
        return visitChildren(ctx);
    }

    override fun visitParam_list(ctx: WACCParser.Param_listContext): Void? {
        println("Param list visit")
        return visitChildren(ctx);
    }

    override fun visitExpr(ctx: WACCParser.ExprContext): Void? {
        println("Expr visit")
        return visitChildren(ctx);
    }

    override fun visitPair_elem(ctx: WACCParser.Pair_elemContext): Void? {
        println("Pair elem visit")
        return visitChildren(ctx);
    }

    override fun visitArray_elem(ctx: WACCParser.Array_elemContext): Void? {
        println("Array elem visit")
        return visitChildren(ctx);
    }

    override fun visitIdent(ctx: WACCParser.IdentContext): Void? {
        println("Array elem visit")
        return visitChildren(ctx);
    }

    override fun visitUnary_op(ctx: WACCParser.Unary_opContext): Void? {
        println("Unary op visit")
        return visitChildren(ctx);
    }

    override fun visitBinary_op(ctx: WACCParser.Binary_opContext): Void? {
        println("Binary op visit")
        return visitChildren(ctx);
    }

    override fun visitProg(ctx: WACCParser.ProgContext): Void? {
        println("Prog visit")
        return visitChildren(ctx);
    }

    override fun visitComment(ctx: WACCParser.CommentContext): Void? {
        println("Comment visit")
        return null;
    }
}
