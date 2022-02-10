package visitor

import antlr.*
import org.antlr.v4.runtime.tree.ParseTree
import expr.*
import stat.*
import symbols.*
import func.*

import kotlin.collections.MutableList

class Visitor : WACCParserBaseVisitor<Identifier>() {

    var functionST : SymbolTable = SymbolTable(null)
    var currentSymbolTable : SymbolTable = SymbolTable(null)
    var returnType: Type? = null

    override fun visitProg(ctx: WACCParser.ProgContext): Identifier? {
        ErrorHandler.setContext(ctx)
        for (i in 1..(ctx.getChildCount() - 3)) {
            visit(ctx.getChild(i))
        }
        val node = visit(ctx.getChild(ctx.getChildCount() - 2))
        for (e in functionST.dict.entries) {
            if (!(e.value is func.Function)) {
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Function ${e.key} is not defined in this scope"
                )
            }
        }
        return node
    }

    //Functions
    override fun visitFunc(ctx: WACCParser.FuncContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val funcName = ctx.IDENT().text
        val funcType = visit(ctx.getChild(0)) as Type
        returnType = funcType

        val funcSymbolTable = SymbolTable(currentSymbolTable)
        currentSymbolTable = funcSymbolTable
        /*     0     1          2              3                4          5  6    7
        func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS stat END;
        */

        val types = mutableListOf<Type?>()
        val paramList = mutableListOf<Parameter>()
        var offset = 0
        if (ctx.param_list() != null) {
            for (i in 0 until ctx.param_list().param().size) {
                val param = ctx.param_list().param()[i]
                val paramType = visit(param.type()) as Type
                val paramName = param.IDENT().text

                types.add(paramType)
                paramList.add(Parameter(paramType, paramName))
                currentSymbolTable.add(paramName, paramType)
            }
            offset = 1
        }

        val ft = functionST.lookup(funcName)
        if (ft == null) {
            val ft = FuncType(functionST, funcName, types.toTypedArray())
            ft.returnType = returnType
            functionST.add(funcName, ft)
        } else {
            if (!(ft is FuncType)) {
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Function $funcName is already defined in this scope"
                )
            }
        }

        val funcBody: Stat = visit(ctx.getChild(5 + offset)) as Stat
        if (!ReturnChecker.check(funcBody)) {
            ErrorHandler.printErr(
                ErrorType.SYNTAX,
                "Function $funcName is not ended with a return or an exit statement"
            )
            System.err.println("$funcName does not return correctly")
            Identifier.valid = false
        }

        currentSymbolTable = currentSymbolTable.getTable()!!
        returnType = null

        val funcParam = ParamList(paramList)

        val funcAST = Function(functionST, funcName, funcType, funcParam, funcSymbolTable, funcBody)

        functionST.add(funcName, funcAST)
        return funcAST
    }

    //Statements
    override fun visitSkip(ctx: WACCParser.SkipContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return Skip
    }

    override fun visitWhile(ctx: WACCParser.WhileContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat: Stat = visit(ctx.getChild(3)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!

        return While(expr, stat)
    }

    override fun visitDeclaration(ctx: WACCParser.DeclarationContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val type: Type = visit(ctx.getChild(0)) as Type
        val id: kotlin.String = ctx.IDENT().text
        val rhs: AssignRhs = visit(ctx.getChild(3)) as AssignRhs

        if (ctx.getChild(3) is WACCParser.AssignFuncContext) {
            val child = ctx.getChild(3) as WACCParser.AssignFuncContext
            val t = functionST.lookup(child.IDENT().text)
            if (t is FuncType) {
                t.returnType = type
                val call = rhs as Call
                call.type = type
            }
        }

        return Declaration(type, id, rhs, currentSymbolTable)
    }

    override fun visitAssignment(ctx: WACCParser.AssignmentContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val lhs = visit(ctx.getChild(0)) as AssignLhs
        val rhs = visit(ctx.getChild(2)) as AssignRhs

        if (ctx.getChild(2) is WACCParser.AssignFuncContext) {
            val child = ctx.getChild(2) as WACCParser.AssignFuncContext
            val t = functionST.lookup(child.IDENT().text)
            if (t is FuncType) {
                t.returnType = lhs.type()
                val call = rhs as Call
                call.type = lhs.type()
            }
        }

        return Assignment(lhs, rhs)
    }

    override fun visitRead(ctx: WACCParser.ReadContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val lhs = visit(ctx.getChild(1)) as AssignLhs
        return Read(lhs)
    }

    override fun visitExit(ctx: WACCParser.ExitContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Exit(expr)
    }

    override fun visitPrint(ctx: WACCParser.PrintContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Println(expr)
    }

    override fun visitPrintln(ctx: WACCParser.PrintlnContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Println(expr)
    }

    override fun visitComposition(ctx: WACCParser.CompositionContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val stat1: Stat = visit(ctx.getChild(0)) as Stat
        val stat2: Stat = visit(ctx.getChild(2)) as Stat
        return Semi(stat1, stat2)
    }

    override fun visitFree(ctx: WACCParser.FreeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Free(expr)
    }

    override fun visitIf(ctx: WACCParser.IfContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat1: Stat = visit(ctx.getChild(3)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!
        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat2: Stat = visit(ctx.getChild(5)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!
        return If(expr, stat1, stat2)
    }

    override fun visitBegin(ctx: WACCParser.BeginContext): Identifier? {
        ErrorHandler.setContext(ctx)
        currentSymbolTable = SymbolTable(currentSymbolTable)
        val stat: Stat = visit(ctx.getChild(1)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!
        return Begin(stat)
    }

    override fun visitReturn(ctx: WACCParser.ReturnContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Return(expr, returnType)
    }

    override fun visitAssignVar(ctx: WACCParser.AssignVarContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return Variable(ctx.IDENT().text, currentSymbolTable)
    }

    override fun visitAssignArrayElem(ctx: WACCParser.AssignArrayElemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitAssignLhsPairElem(ctx: WACCParser.AssignLhsPairElemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitAssignExpr(ctx: WACCParser.AssignExprContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitAssignPair(ctx: WACCParser.AssignPairContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val e1: Expr = visit(ctx.getChild(2)) as Expr
        val e2: Expr = visit(ctx.getChild(4)) as Expr
        return NewPair(e1, e2)
    }

    override fun visitAssignPairElem(ctx: WACCParser.AssignPairElemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitAssignFunc(ctx: WACCParser.AssignFuncContext): Identifier? {
        ErrorHandler.setContext(ctx)
        var values = arrayOf<Expr>()
        if (ctx.arg_list() != null) {
            values = (visit(ctx.getChild(3)) as ArgList).values
        }
        return Call(values, ctx.IDENT().text, functionST)
    }

    //Types
    override fun visitArray_literal(ctx: WACCParser.Array_literalContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val values = mutableListOf<Expr>()
        for (i in 1..(ctx.getChildCount() - 2) step 2) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }
        var node: Expr = EmptyArrayLiteral
        if (values.size > 0) {
            node = ArrayLiteral(values.toTypedArray(), values[0].type())
        }
        return node
    }

    override fun visitBaseType(ctx: WACCParser.BaseTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitPairType(ctx: WACCParser.PairTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    // TODO: figure out how to deal with number of elements
    override fun visitArray_type(ctx: WACCParser.Array_typeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t: Type = visit(ctx.getChild(0)) as Type
        return symbols.ArrayInstance(t, 0)
    }

    override fun visitArrayType(ctx: WACCParser.ArrayTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t: Type = visit(ctx.getChild(0)) as Type
        return symbols.ArrayInstance(t, 0)
    }

    override fun visitIntType(ctx: WACCParser.IntTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return symbols.Int
    }

    override fun visitBoolType(ctx: WACCParser.BoolTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return symbols.Boolean
    }

    override fun visitCharType(ctx: WACCParser.CharTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return symbols.Char
    }

    override fun visitStringType(ctx: WACCParser.StringTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return symbols.String
    }

    override fun visitPairBaseType(ctx: WACCParser.PairBaseTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitPairArrayType(ctx: WACCParser.PairArrayTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitPairPairType(ctx: WACCParser.PairPairTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return symbols.TypelessPair
    }

    override fun visitPair_type(ctx: WACCParser.Pair_typeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t1: Type = visit(ctx.getChild(2)) as Type
        val t2: Type = visit(ctx.getChild(4)) as Type
        return symbols.PairInstance(t1, t2)
    }

    override fun visitArg_list(ctx: WACCParser.Arg_listContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val values = mutableListOf<Expr>()
        for (i in 0..(ctx.getChildCount() - 1) step 2) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }
        return ArgList(values.toTypedArray())
    }

    override fun visitParam(ctx: WACCParser.ParamContext): Identifier? {
        ErrorHandler.setContext(ctx)
        println("Param visit")
        return visitChildren(ctx)
    }

    override fun visitParam_list(ctx: WACCParser.Param_listContext): Identifier? {
        ErrorHandler.setContext(ctx)
        println("Param list visit")
        return visitChildren(ctx)
    }

    override fun visitPair_elem(ctx: WACCParser.Pair_elemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val e: Expr = visit(ctx.getChild(1)) as Expr
        return PairElem(ctx.getChild(0).text, e)
    }

    override fun visitArray_elem(ctx: WACCParser.Array_elemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val values = mutableListOf<Expr>()
        for (i in 2..(ctx.getChildCount() - 2) step 3) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }

        return ArrayElem(
            ctx.IDENT().text,
            values.toTypedArray(),
            ((ctx.getChildCount() - 1) / 3),
            currentSymbolTable.lookupAll(ctx.IDENT().text) as Type?
        )
    }

    override fun visitPairLiteral(ctx: WACCParser.PairLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return PairLiteral()
    }

    //binary ops

    override fun visitExpr6(ctx: WACCParser.Expr6Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop6() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).getText()) {
                "||" -> node = Or(expr1, expr2)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitExpr5(ctx: WACCParser.Expr5Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop5() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).getText()) {
                "&&" -> node = And(expr1, expr2)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitExpr4(ctx: WACCParser.Expr4Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop4() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).getText()) {
                "==" -> node = Equiv(expr1, expr2)
                "!=" -> node = NotEquiv(expr1, expr2)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitExpr3(ctx: WACCParser.Expr3Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop3() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).getText()) {
                ">" -> node = Gt(expr1, expr2)
                ">=" -> node = Gte(expr1, expr2)
                "<" -> node = Lt(expr1, expr2)
                "<=" -> node = Lte(expr1, expr2)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitExpr2(ctx: WACCParser.Expr2Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop2() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).getText()) {
                "+" -> node = Plus(expr1, expr2)
                "-" -> node = Minus(expr1, expr2)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp(ctx: WACCParser.BinaryOpContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr
        val expr2: Expr = visit(ctx.getChild(2)) as Expr

        var node: Identifier
        when (ctx.getChild(1).getText()) {
            "*" -> node = Multi(expr1, expr2)
            "/" -> node = Div(expr1, expr2)
            "%" -> node = Mod(expr1, expr2)
            else -> throw Exception("Not Reachable")
        }
        return node
    }

    override fun visitInt_literal(ctx: WACCParser.Int_literalContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val token = ctx.text
        return IntLiteral(token)
    }

    override fun visitIntLiteral(ctx: WACCParser.IntLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitBoolLiteral(ctx: WACCParser.BoolLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val token = ctx.bool_literal().text
        return BooleanLiteral(token)
    }

    override fun visitCharLiteral(ctx: WACCParser.CharLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val token = ctx.CHAR_LITERAL().symbol.text
        return CharLiteral(token)
    }

    override fun visitStringLiteral(ctx: WACCParser.StringLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val token = ctx.STRING_LITERAL().symbol.text
        return StringLiteral(token)
    }

    override fun visitArrayLiteral(ctx: WACCParser.ArrayLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitIdentifier(ctx: WACCParser.IdentifierContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return Variable(ctx.IDENT().text, currentSymbolTable)
    }

    override fun visitArrayElem(ctx: WACCParser.ArrayElemContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(0))
    }

    override fun visitUnaryOp(ctx: WACCParser.UnaryOpContext): Identifier? {
        ErrorHandler.setContext(ctx)
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
        return node
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(1))
    }
}
