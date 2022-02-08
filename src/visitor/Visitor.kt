package visitor

import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import expr.*
import stat.*
import symbols.*
import func.*

class Visitor : WACCParserBaseVisitor<Identifier>() {

    var currentSymbolTable : SymbolTable = SymbolTable(null)
    var valid = true
    var returnType: Type? = null

    init {
        currentSymbolTable.add("int", symbols.Int)
        currentSymbolTable.add("bool", symbols.Boolean)
        currentSymbolTable.add("char", symbols.Char)
        currentSymbolTable.add("null", symbols.Null)
        currentSymbolTable.add("string", symbols.String)
    }

    //Statements
    override fun visitSkip(ctx: WACCParser.SkipContext): Identifier? {
        return Skip()
    }

    override fun visitWhile(ctx: WACCParser.WhileContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat: Stat = visit(ctx.getChild(3)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!

        val node = While(expr, stat)
        if (!node.valid) {
            System.err.println("Error in while")
            valid = false
        }
        return node
    }

    override fun visitDeclaration(ctx: WACCParser.DeclarationContext): Identifier? {
        val type: Type = currentSymbolTable.lookupAll(ctx.type().text) as Type
        val id: kotlin.String = ctx.IDENT().text
        val rhs: AssignRhs = visit(ctx.getChild(3)) as AssignRhs

        val node = Declaration(type, id, rhs, currentSymbolTable)
        if (!node.valid) {
            System.err.println("Error in declaration")
            valid = false
        }
        return node
    }

    override fun visitExit(ctx: WACCParser.ExitContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node = Exit(expr)
        if (!node.valid) {
            System.err.println("Error in exit")
            valid = false
        }
        return node
    }

    override fun visitPrint(ctx: WACCParser.PrintContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node = Println(expr)
        if (!node.valid) {
            System.err.println("Error in print")
            valid = false
        }
        return node
    }

    override fun visitPrintln(ctx: WACCParser.PrintlnContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node = Println(expr)
        if (!node.valid) {
            System.err.println("Error in println")
            valid = false
        }
        return node
    }

    override fun visitComposition(ctx: WACCParser.CompositionContext): Identifier? {
        val stat1: Stat = visit(ctx.getChild(0)) as Stat
        val stat2: Stat = visit(ctx.getChild(2)) as Stat

        val node = Semi(stat1, stat2)
        if (!node.valid) {
            System.err.println("Error in composition")
            valid = false
        }
        return node
    }

    override fun visitFree(ctx: WACCParser.FreeContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node = Free(expr)
        if (!node.valid) {
            System.err.println("Error in free")
            valid = false
        }
        return node
    }

    override fun visitIf(ctx: WACCParser.IfContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        val stat1: Stat = visit(ctx.getChild(3)) as Stat
        val stat2: Stat = visit(ctx.getChild(5)) as Stat

        val node = If(expr, stat1, stat2)
        if (!node.valid) {
            System.err.println("Error in if")
            valid = false
        }
        return node
    }

    override fun visitBegin(ctx: WACCParser.BeginContext): Identifier? {
        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat: Stat = visit(ctx.getChild(1)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!

        val node = Begin(stat)
        if (!node.valid) {
            System.err.println("Error in begin")
            valid = false
        }
        return node
    }

    override fun visitReturn(ctx: WACCParser.ReturnContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node = Return(expr, returnType)
        if (!node.valid) {
            System.err.println("Error in return")
            valid = false
        }
        return node
    }

    override fun visitAssign_lhs(ctx: WACCParser.Assign_lhsContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitAssignExpr(ctx: WACCParser.AssignExprContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitAssignPair(ctx: WACCParser.AssignPairContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitAssignPairElem(ctx: WACCParser.AssignPairElemContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitAssignFunc(ctx: WACCParser.AssignFuncContext): Identifier? {
        return visitChildren(ctx)
    }

    //Types
    override fun visitArray_literal(ctx: WACCParser.Array_literalContext): Identifier? {
        println("Array literal visit")
        return visitChildren(ctx)
    }

    override fun visitBase_type(ctx: WACCParser.Base_typeContext): Identifier? {
        println("Base type visit")
        return visitChildren(ctx)
    }

    override fun visitPair_elem_type(ctx: WACCParser.Pair_elem_typeContext): Identifier? {
        println("Pair elem type visit")
        return visitChildren(ctx)
    }

    override fun visitPair_type(ctx: WACCParser.Pair_typeContext): Identifier? {
        println("Pair type visit")
        return visitChildren(ctx)
    }

    override fun visitType(ctx: WACCParser.TypeContext): Identifier? {
        println("Type visit")
        return visitChildren(ctx)
    }

    //Functions
    override fun visitFunc(ctx: WACCParser.FuncContext): Identifier? {
        val funcName = ctx.IDENT().text
        val funcType = currentSymbolTable.lookupAll(ctx.type().text) as Type
        returnType = funcType

        val funcSymbolTable = SymbolTable(currentSymbolTable)
        currentSymbolTable = funcSymbolTable
        /*     0     1          2              3                4          5  6    7
        func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
        */
        val funcBody : Stat = visit(ctx.getChild(6)) as Stat
        //change current symbol table back to its parent
        currentSymbolTable = currentSymbolTable.getTable()!!
        returnType = null

        val paramList = mutableListOf<Parameter>()
        for (i in 0 until ctx.param_list().param().size) {
            val param = ctx.param_list().param()[i]
            val paramType = currentSymbolTable.lookupAll(param.type().text) as Type
            val paramName = param.IDENT().text

            //add to paramList
            paramList.add(Parameter(paramType, paramName))
            //add to function's symbol table
            funcSymbolTable.add(paramName, paramType)
        }

        val funcParam = ParamList(paramList)

        val funcAST = Function(currentSymbolTable,funcName,funcType,funcParam,funcSymbolTable,funcBody)
        if (!funcAST.valid) {
            System.err.println("$funcName already defined in current scope")
            valid = false;
        }

        currentSymbolTable.add(funcName, funcAST)
        return funcAST
    }

    override fun visitArg_list(ctx: WACCParser.Arg_listContext): Identifier? {
        println("Arg list visit")
        return visitChildren(ctx)
    }

    override fun visitParam(ctx: WACCParser.ParamContext): Identifier? {
        println("Param visit")
        return visitChildren(ctx)
    }

    override fun visitParam_list(ctx: WACCParser.Param_listContext): Identifier? {
        println("Param list visit")
        return visitChildren(ctx)
    }

    override fun visitPair_elem(ctx: WACCParser.Pair_elemContext): Identifier? {
        println("Pair elem visit")
        return visitChildren(ctx)
    }

    override fun visitArray_elem(ctx: WACCParser.Array_elemContext): Identifier? {
        println("Array elem visit")
        return visitChildren(ctx)
    }

    //Unary operators
    //error messages will be modified
    override fun visitExclamation(ctx: WACCParser.ExclamationContext): Identifier? {
        println("Unary op exclamation visit")

        val childExpr: Expr = visit(ctx.getChild(0)) as Expr;
        val exclamationAST = Exclamation(childExpr);

        val astValid = exclamationAST.valid;
        if (!astValid) {
            System.err.println("Error in visitExclamation");
        }

        //not a fan of doing this for every single visit method
        valid = valid and astValid

        return exclamationAST
    }

    override fun visitLen(ctx: WACCParser.LenContext): Identifier? {
        println("Unary op len visit")
        return visitChildren(ctx)
    }

    override fun visitOrd(ctx: WACCParser.OrdContext): Identifier? {
        println("Unary op ord visit")
        return visitChildren(ctx)
    }
    override fun visitChr(ctx: WACCParser.ChrContext): Identifier? {
        println("Unary op chr visit")
        return visitChildren(ctx)
    }

    override fun visitProg(ctx: WACCParser.ProgContext): Identifier? {
        println("Prog visit")
        return visitChildren(ctx)
    }

    override fun visitPlus(ctx: WACCParser.PlusContext) : Identifier? {
        println("Plus binary op visit")
        return visitChildren(ctx)
    }

    override fun visitNeg(ctx: WACCParser.NegContext) : Identifier? {
        println("Minus binary op visit")
        return visitChildren(ctx)
    }

    override fun visitMulti(ctx: WACCParser.MultiContext) : Identifier? {
        println("Multiply binary op visit")
        return visitChildren(ctx)
    }

    override fun visitDiv(ctx: WACCParser.DivContext) : Identifier? {
        println("Divide binary op visit")
        return visitChildren(ctx)
    }

    override fun visitMod(ctx: WACCParser.ModContext) : Identifier? {
        println("Modulo binary op visit")
        return visitChildren(ctx)
    }

    override fun visitGt(ctx: WACCParser.GtContext) : Identifier? {
        println("Greater than binary op visit")
        return visitChildren(ctx)
    }

    override fun visitGte(ctx: WACCParser.GteContext) : Identifier? {
        println("Greater than or equal binary op visit")
        return visitChildren(ctx)
    }

    override fun visitLt(ctx: WACCParser.LtContext) : Identifier? {
        println("Less than binary op visit")
        return visitChildren(ctx)
    }

    override fun visitLte(ctx: WACCParser.LteContext) : Identifier? {
        println("Less than or equal binary op visit")
        return visitChildren(ctx)
    }

    override fun visitEquiv(ctx: WACCParser.EquivContext) : Identifier? {
        println("Equivalent binary op visit")
        return visitChildren(ctx)
    }

    override fun visitNotequiv(ctx: WACCParser.NotequivContext) : Identifier? {
        println("Not equivalent binary op visit")
        return visitChildren(ctx)
    }

    override fun visitAnd(ctx: WACCParser.AndContext) : Identifier? {
        println("And binary op visit")
        return visitChildren(ctx)
    }

    override fun visitOr(ctx: WACCParser.OrContext) : Identifier? {
        println("Or binary op visit")
        return visitChildren(ctx)
    }

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): Identifier? {
        println("Expr::IntLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): Identifier? {
        println("Expr::BoolLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): Identifier? {
        println("Expr::CharLiteral visit")
        val chr = ctx.CHAR_LITERAL().symbol.text
        val chrAST = CharLiteral(chr)
        val astValid = chrAST.valid
        if(!chrAST.valid){
            System.err.println(ctx.CHAR_LITERAL().symbol.line.toString() + "ERROROROROOROO")
        }
        valid = astValid && valid
        return chrAST
    }

    override fun visitStringLiteral(ctx: WACCParser.StringLiteralContext): Identifier? {
        println("Expr::StringLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitArrayLiteral(ctx: WACCParser.ArrayLiteralContext): Identifier? {
        println("Expr::ArrayLiteral visit")
        return visitChildren(ctx)
    }

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): Identifier? {
        println("Expr::Identifier visit")
        return visitChildren(ctx)
    }

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): Identifier? {
        println("Expr::ArrayElem visit")
        return visitChildren(ctx)
    }

    override fun visitUnaryOp(ctx: WACCParser.UnaryOpContext): Identifier? {
        println("Expr::UnaryOp visit")
        return visitChildren(ctx)
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Identifier? {
        println("Expr::Parens visit")
        return visitChildren(ctx)
    }
}
