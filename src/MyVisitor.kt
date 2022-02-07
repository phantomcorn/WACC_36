package myvisitor

import SymbolTable
import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.RuleNode

class MyVisitor : WACCParserBaseVisitor<Void>() {

    var currentSymbolTable : SymbolTable = SymbolTable(null);

    override fun visit(tree: ParseTree): Void? {
        return super.visit(tree)
    }

    override fun visitSkip(ctx: WACCParser.SkipContext): Void? {
        println("Skip statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitWhile(ctx: WACCParser.WhileContext): Void? {
        println("While statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitDeclaration(ctx: WACCParser.DeclarationContext?): Void? {
        println("Declaration statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitExit(ctx: WACCParser.ExitContext?): Void? {
        println("Exit statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitPrint(ctx: WACCParser.PrintContext?): Void? {
        println("Print statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitPrintln(ctx: WACCParser.PrintlnContext?): Void? {
        println("Println statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitComposition(ctx: WACCParser.CompositionContext?): Void? {
        println("Composition statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitFree(ctx: WACCParser.FreeContext?): Void? {
        println("Free statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitIf(ctx: WACCParser.IfContext?): Void? {
        println("If statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitBegin(ctx: WACCParser.BeginContext?): Void? {
        println("Begin statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitReturn(ctx: WACCParser.ReturnContext?): Void? {
        println("Return statement visit")
        val result = visitChildren(ctx)
        return result;
    }

    override fun visitAssign_lhs(ctx: WACCParser.Assign_lhsContext): Void? {
        println("Assign lhs visit")
        return visitChildren(ctx);
    }

        
    override fun visitAssign_rhs(ctx: WACCParser.Assign_rhsContext): Void? {
        println("Assign rhs visit")
        return visitChildren(ctx);
    }

    override fun visitArray_literal(ctx: WACCParser.Array_literalContext): Void? {
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

    override fun visitPair_elem(ctx: WACCParser.Pair_elemContext): Void? {
        println("Pair elem visit")
        return visitChildren(ctx);
    }

    override fun visitArray_elem(ctx: WACCParser.Array_elemContext): Void? {
        println("Array elem visit")
        return visitChildren(ctx);
    }

    override fun visitExclamation(ctx: WACCParser.ExclamationContext): Void? {
        println("Unary op exclamation visit")
        return visitChildren(ctx);
    }

    override fun visitMin(ctx: WACCParser.MinContext): Void? {
        println("Unary op minus visit")
        return visitChildren(ctx);
    }

    override fun visitLen(ctx: WACCParser.LenContext): Void? {
        println("Unary op len visit")
        return visitChildren(ctx);
    }

    override fun visitOrd(ctx: WACCParser.OrdContext): Void? {
        println("Unary op ord visit")
        return visitChildren(ctx);
    }
    override fun visitChr(ctx: WACCParser.ChrContext): Void? {
        println("Unary op chr visit")
        return visitChildren(ctx);
    }

    override fun visitProg(ctx: WACCParser.ProgContext): Void? {
        println("Prog visit")
        return visitChildren(ctx);
    }

    override fun visitPlus(ctx: WACCParser.PlusContext) : Void? {
        println("Plus binary op visit")
        return visitChildren(ctx)
    }

    override fun visitMinus(ctx: WACCParser.MinusContext) : Void? {
        println("Minus binary op visit")
        return visitChildren(ctx)
    }

    override fun visitMulti(ctx: WACCParser.MultiContext) : Void? {
        println("Multiply binary op visit")
        return visitChildren(ctx)
    }

    override fun visitDiv(ctx: WACCParser.DivContext) : Void? {
        println("Divide binary op visit")
        return visitChildren(ctx)
    }

    override fun visitMod(ctx: WACCParser.ModContext) : Void? {
        println("Modulo binary op visit")
        return visitChildren(ctx)
    }

    override fun visitGt(ctx: WACCParser.GtContext) : Void? {
        println("Greater than binary op visit")
        return visitChildren(ctx)
    }

    override fun visitGte(ctx: WACCParser.GteContext) : Void? {
        println("Greater than or equal binary op visit")
        return visitChildren(ctx)
    }

    override fun visitLt(ctx: WACCParser.LtContext) : Void? {
        println("Less than binary op visit")
        return visitChildren(ctx)
    }

    override fun visitLte(ctx: WACCParser.LteContext) : Void? {
        println("Less than or equal binary op visit")
        return visitChildren(ctx)
    }

    override fun visitEquiv(ctx: WACCParser.EquivContext) : Void? {
        println("Equivalent binary op visit")
        return visitChildren(ctx)
    }

    override fun visitNotequiv(ctx: WACCParser.NotequivContext) : Void? {
        println("Not equivalent binary op visit")
        return visitChildren(ctx)
    }

    override fun visitAnd(ctx: WACCParser.AndContext) : Void? {
        println("And binary op visit")
        return visitChildren(ctx)
    }

    override fun visitOr(ctx: WACCParser.OrContext) : Void? {
        println("Or binary op visit")
        return visitChildren(ctx)
    }

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): Void? {
        println("Expr::IntLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): Void? {
        println("Expr::BoolLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): Void? {
        println("Expr::CharLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitStringLiteral(ctx: WACCParser.StringLiteralContext): Void? {
        println("Expr::StringLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitArrayLiteral(ctx: WACCParser.ArrayLiteralContext): Void? {
        println("Expr::ArrayLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): Void? {
        println("Expr::Identifier visit")
        return visitChildren(ctx)
    }

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): Void? {
        println("Expr::ArrayElem visit")
        return visitChildren(ctx)
    }

    override fun visitUnaryOp(ctx: WACCParser.UnaryOpContext): Void? {
        println("Expr::UnaryOp visit")
        return visitChildren(ctx)
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Void? {
        println("Expr::Parens visit")
        return visitChildren(ctx)
    }
}
