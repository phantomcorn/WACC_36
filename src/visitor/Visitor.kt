package visitor

import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import expr.*
import stat.*
import symbols.*
import func.*

import kotlin.collections.MutableList

class Visitor : WACCParserBaseVisitor<Identifier>() {

    var currentSymbolTable : SymbolTable = SymbolTable(null)
    var valid = true
    var returnType: Type? = null

    override fun visitProg(ctx: WACCParser.ProgContext): Identifier? {
        return visitChildren(ctx)
    }

    //Functions
    override fun visitFunc(ctx: WACCParser.FuncContext): Identifier? {
        val funcName = ctx.IDENT().text
        val funcType = visit(ctx.getChild(0)) as Type
        returnType = funcType

        val funcSymbolTable = SymbolTable(currentSymbolTable)
        currentSymbolTable = funcSymbolTable
        /*     0     1          2              3                4          5  6    7
        func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
        */

        val paramList = mutableListOf<Parameter>()
        for (i in 0 until ctx.param_list().param().size) {
            val param = ctx.param_list().param()[i]
            val paramType = visit(param.type()) as Type
            val paramName = param.IDENT().text

            paramList.add(Parameter(paramType, paramName))
            currentSymbolTable.add(paramName, paramType)
        }

        val funcBody : Stat = visit(ctx.getChild(6)) as Stat

        currentSymbolTable = currentSymbolTable.getTable()!!
        returnType = null

        val funcParam = ParamList(paramList)

        val funcAST = Function(currentSymbolTable, funcName, funcType, funcParam, funcSymbolTable, funcBody)
        if (!funcAST.valid) {
            System.err.println("$funcName already defined in current scope")
            valid = false
        }

        currentSymbolTable.add(funcName, funcAST)
        return funcAST
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
        val type: Type = visit(ctx.getChild(0)) as Type
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
        val e1: Expr = visit(ctx.getChild(2)) as Expr
        val e2: Expr = visit(ctx.getChild(4)) as Expr
        return NewPair(e1, e2)
    }

    override fun visitAssignPairElem(ctx: WACCParser.AssignPairElemContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitAssignFunc(ctx: WACCParser.AssignFuncContext): Identifier? {
        val values = (visit(ctx.getChild(3)) as ArgList).values
        val node = Call(values, ctx.IDENT().text, currentSymbolTable)
        if (!node.valid) {
            System.err.println("Error in call")
            valid = false
        }
        return node
    }

    //Types
    override fun visitArray_literal(ctx: WACCParser.Array_literalContext): Identifier? {
        val values = mutableListOf<Expr>()
        for (i in 1..(ctx.getChildCount() - 2) step 2) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }
        var node: Expr = EmptyArrayLiteral
        if (values.size > 0) {
            node = ArrayLiteral(values.toTypedArray(), values[0].type())
        }
        if (!node.valid) {
            System.err.println("Error in array literal")
            valid = false
        }
        return node
    }

    override fun visitBaseType(ctx: WACCParser.BaseTypeContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitPairType(ctx: WACCParser.PairTypeContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    // TODO: figure out how to deal with number of elements
    override fun visitArray_type(ctx: WACCParser.Array_typeContext): Identifier? {
        val t: Type = visit(ctx.getChild(0)) as Type
        return symbols.Array(t, 0)
    }

    override fun visitArrayType(ctx: WACCParser.ArrayTypeContext): Identifier? {
        val t: Type = visit(ctx.getChild(0)) as Type
        return symbols.Array(t, 0)
    }

    override fun visitIntType(ctx: WACCParser.IntTypeContext): Identifier? {
        return symbols.Int
    }

    override fun visitBoolType(ctx: WACCParser.BoolTypeContext): Identifier? {
        return symbols.Boolean
    }

    override fun visitCharType(ctx: WACCParser.CharTypeContext): Identifier? {
        return symbols.Char
    }

    override fun visitStringType(ctx: WACCParser.StringTypeContext): Identifier? {
        return symbols.String
    }

    override fun visitPairBaseType(ctx: WACCParser.PairBaseTypeContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitPairArrayType(ctx: WACCParser.PairArrayTypeContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitPairPairType(ctx: WACCParser.PairPairTypeContext): Identifier? {
        return symbols.TypelessPair
    }

    override fun visitPair_type(ctx: WACCParser.Pair_typeContext): Identifier? {
        val t1: Type = visit(ctx.getChild(2)) as Type
        val t2: Type = visit(ctx.getChild(4)) as Type
        return symbols.Pair(t1, t2)
    }

    override fun visitArg_list(ctx: WACCParser.Arg_listContext): Identifier? {
        val values = mutableListOf<Expr>()
        for (i in 0..(ctx.getChildCount() - 1) step 2) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }
        return ArgList(values.toTypedArray())
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

    //binary ops

    override fun visitBinaryOp(ctx: WACCParser.BinaryOpContext): Identifier? {
        val expr1: Expr = visit(ctx.getChild(0)) as Expr
        val expr2: Expr = visit(ctx.getChild(2)) as Expr

        var node: Identifier
        when (ctx.getChild(1).getText()) {
            "*" -> node = Multi(expr1, expr2)
            "/" -> node = Div(expr1, expr2)
            "%" -> node = Mod(expr1, expr2)
            "+" -> node = Plus(expr1, expr2)
            "-" -> node = Minus(expr1, expr2)
            ">" -> node = Gt(expr1, expr2)
            ">=" -> node = Gte(expr1, expr2)
            "<" -> node = Lt(expr1, expr2)
            "<=" -> node = Lte(expr1, expr2)
            "==" -> node = Equiv(expr1, expr2)
            "!=" -> node = NotEquiv(expr1, expr2)
            "&&" -> node = And(expr1, expr2)
            "||" -> node = Or(expr1, expr2)
            else -> throw Exception("Not Reachable")
        }

        if (!node.valid) {
            System.err.println("Error in binary op")
            valid = false
        }

        return node
    }

    override fun visitInt_literal(ctx: WACCParser.Int_literalContext): Identifier? {
        val token = ctx.INT_LITERAL().text
        val node = IntLiteral(token)
        if (!node.valid) {
            System.err.println("Error in int literal")
            valid = false
        }
        return node
    }

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): Identifier? {
        val token = ctx.bool_literal().text
        val node = BooleanLiteral(token)
        if (!node.valid) {
            System.err.println("Error in bool literal")
            valid = false
        }
        return node
    }

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): Identifier? {
        val token = ctx.CHAR_LITERAL().symbol.text
        val node = CharLiteral(token)
        if (!node.valid) {
            System.err.println("Error in char literal")
            valid = false
        }
        return node
    }

    override fun visitStringLiteral(ctx: WACCParser.StringLiteralContext): Identifier? {
        val token = ctx.STRING_LITERAL().symbol.text
        val node = StringLiteral(token)
        if (!node.valid) {
            System.err.println("Error in string literal")
            valid = false
        }
        return node
    }

    override fun visitArrayLiteral(ctx: WACCParser.ArrayLiteralContext): Identifier? {
        return visit(ctx.getChild(0))
    }

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): Identifier? {
        val node = Variable(ctx.IDENT().text, currentSymbolTable)
        if (!node.valid) {
            System.err.println("Error in identifier")
            valid = false
        }
        return node
    }

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): Identifier? {
        println("Expr::ArrayElem visit")
        return visitChildren(ctx)
    }

    override fun visitUnaryOp(ctx: WACCParser.UnaryOpContext): Identifier? {
        val expr: Expr = visit(ctx.getChild(1)) as Expr;

        var node: Identifier
        when (ctx.getChild(0).getText()) {
            "!" -> node = Not(expr)
            "-" -> node = Neg(expr)
            "len" -> node = Len(expr)
            "ord" -> node = Ord(expr)
            "chr" -> node = Chr(expr)
            else -> throw Exception("Not Reachable")
        }

        if (!node.valid) {
            System.err.println("Error in unary op")
            valid = false
        }

        return node
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Identifier? {
        return visit(ctx.getChild(1))
    }
}
