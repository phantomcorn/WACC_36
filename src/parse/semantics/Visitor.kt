package parse.semantics

import ErrorHandler
import ErrorType
import antlr.WACCParser
import antlr.WACCParserBaseVisitor
import codegen.instr.Add
import parse.expr.*
import parse.func.*
import parse.func.Function
import parse.stat.*
import parse.symbols.*
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int


class Visitor : WACCParserBaseVisitor<Identifier>() {

    var functionST: SymbolTable<Function> = SymbolTable(null)
    var currentSymbolTable: SymbolTable<Type> = SymbolTable(null)

    override fun visitProg(ctx: WACCParser.ProgContext): Identifier? {
        ErrorHandler.setContext(ctx)

        /* PARSER RULE prog : BEGIN  parse.func* parse.stat  END EOF;

                              0     1    2   3
        prog with 0 parse.func :  BEGIN parse.stat  END EOF;

                      last parse.func at index : 0

                              0     1    2   3   4
                  1 parse.func :  BEGIN parse.func parse.stat END EOF;

                       last parse.func at index : 1

                              0    1..n   n+1  n+2 n+3
                  n parse.func :  BEGIN  parse.func   parse.stat END EOF;

                        last parse.func at index : n


         */
        val lastFuncIndex = ctx.childCount - 4
        //1st pass : iterate through each function declaration and add temporary placeholder for them
        for (i in 1..lastFuncIndex) {
            val childFuncContext = ctx.getChild(i)
            if (childFuncContext is WACCParser.FuncContext) {
                val funcName = childFuncContext.IDENT().text
                val funcType = visit(childFuncContext.type()) as Type?
                val types = mutableListOf<Type?>()
                if (childFuncContext.param_list() != null) {
                    //in each function, iterate through its parameter
                    for (j in 0 until childFuncContext.param_list().param().size) {
                        val param = childFuncContext.param_list().param()[j]
                        val paramType = visit(param.type()) as Type?
                        types.add(paramType)
                    }
                }
                val ft = FuncType(functionST, funcName, types.toTypedArray(), funcType)
                functionST.add(funcName, ft)
            }
        }

        /* 2nd pass : visit all the function and create actual function node for it in the function symbol table
           - to solve calling a function within a function problem
        */
        for (i in 1..lastFuncIndex) {
            visit(ctx.getChild(i))
        }


        val statBody = ctx.getChild(ctx.childCount - 3)
        //visit the main body of the program
        val node = visit(statBody)
        for (e in functionST.dict.entries) {
            if (!(e.value is parse.func.FuncAST)) {
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
        val funcType = visit(ctx.getChild(0)) as Type?

        val funcSymbolTable = SymbolTable(currentSymbolTable)
        currentSymbolTable = funcSymbolTable

        if (funcType != null) {
            currentSymbolTable.add("$", funcType)
        }
        /*     0     1          2              3                4          5  6    7
        parse.func: type IDENT OPEN_PARENTHESES (param_list)? CLOSE_PARENTHESES IS parse.stat END;
        */

        val types = mutableListOf<Type?>()
        val paramList = mutableListOf<Parameter>()
        var offset = 0
        if (ctx.param_list() != null) {
            for (i in 0 until ctx.param_list().param().size) {
                val param = ctx.param_list().param()[i]
                val paramType = visit(param.type()) as Type?
                val paramName = param.IDENT().text

                types.add(paramType)
                paramList.add(Parameter(paramType, paramName))
                if (paramType != null) {
                    currentSymbolTable.add(paramName, paramType)
                }
            }
            offset = 1
        }

        val funcBody: Stat = visit(ctx.getChild(5 + offset)) as Stat
        if (!ReturnChecker.check(funcBody)) {
            ErrorHandler.printErr(
                ErrorType.SYNTAX,
                "Function $funcName is not ended with a return or an exit statement"
            )
        }

        currentSymbolTable = currentSymbolTable.getTable()!!

        val funcParam = ParamList(paramList)

        val funcAST = FuncAST(functionST, funcName, funcType, funcParam, funcBody, funcSymbolTable)

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

        val st = SymbolTable(currentSymbolTable)
        currentSymbolTable = st
        val stat: Stat = visit(ctx.getChild(3)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!

        return While(expr, stat, st)
    }

    override fun visitDeclaration(ctx: WACCParser.DeclarationContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val type: Type = visit(ctx.getChild(0)) as Type
        val id: kotlin.String = ctx.IDENT().text
        val rhs: AssignRhs = visit(ctx.getChild(3)) as AssignRhs

        return Declaration(type, id, rhs, currentSymbolTable)
    }

    override fun visitAssignment(ctx: WACCParser.AssignmentContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val lhs = visit(ctx.getChild(0)) as AssignLhs
        val rhs = visit(ctx.getChild(2)) as AssignRhs

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
        return Print(expr)
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
        return StatList(stat1, stat2)
    }

    override fun visitFree(ctx: WACCParser.FreeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Free(expr)
    }

    override fun visitIf(ctx: WACCParser.IfContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val st1 = SymbolTable(currentSymbolTable)
        currentSymbolTable = st1
        val stat1: Stat = visit(ctx.getChild(3)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!

        val st2 = SymbolTable(currentSymbolTable)
        currentSymbolTable = st2
        val stat2: Stat = visit(ctx.getChild(5)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!
        return If(expr, stat1, stat2, st1, st2)
    }

    override fun visitBegin(ctx: WACCParser.BeginContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val st = SymbolTable(currentSymbolTable)
        currentSymbolTable = st
        val stat: Stat = visit(ctx.getChild(1)) as Stat
        currentSymbolTable = currentSymbolTable.getTable()!!
        return Begin(stat, st)
    }

    override fun visitReturn(ctx: WACCParser.ReturnContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr: Expr = visit(ctx.getChild(1)) as Expr
        return Return(expr, currentSymbolTable.lookupAll("$"))
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
        for (i in 1..(ctx.childCount - 2) step 2) {
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

    override fun visitArray_type(ctx: WACCParser.Array_typeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t: Type = visit(ctx.getChild(0)) as Type
        return parse.symbols.ArrayInstance(t)
    }

    override fun visitArrayType(ctx: WACCParser.ArrayTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t: Type = visit(ctx.getChild(0)) as Type
        return parse.symbols.ArrayInstance(t)
    }

    override fun visitIntType(ctx: WACCParser.IntTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return parse.symbols.Int
    }

    override fun visitBoolType(ctx: WACCParser.BoolTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return parse.symbols.Boolean
    }

    override fun visitCharType(ctx: WACCParser.CharTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return parse.symbols.Char
    }

    override fun visitStringType(ctx: WACCParser.StringTypeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return parse.symbols.String
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
        return parse.symbols.TypelessPair
    }

    override fun visitPair_type(ctx: WACCParser.Pair_typeContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val t1: Type = visit(ctx.getChild(2)) as Type
        val t2: Type = visit(ctx.getChild(4)) as Type
        return parse.symbols.PairInstance(t1, t2)
    }

    override fun visitArg_list(ctx: WACCParser.Arg_listContext): Identifier? {
        ErrorHandler.setContext(ctx)
        val values = mutableListOf<Expr>()
        for (i in 0..(ctx.childCount - 1) step 2) {
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
        for (i in 2..(ctx.childCount - 2) step 3) {
            values.add(visit(ctx.getChild(i)) as Expr)
        }
        /*    0   1 2 3
            IDENT [ _ ] +

            n dimension  = 3n + 1 child
        */
        val arrayDimension = (ctx.childCount - 1) / 3
        return ArrayElem(
            ctx.IDENT().text,
            values.toTypedArray(),
            arrayDimension,
            currentSymbolTable.lookupAll(ctx.IDENT().text)
        )
    }

    override fun visitPairLiteral(ctx: WACCParser.PairLiteralContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return PairLiteral()
    }

    //binary ops

    override fun visitBinaryOp6(ctx: WACCParser.BinaryOp6Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop6() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).text) {
                "||" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.OR)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp5(ctx: WACCParser.BinaryOp5Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop5() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).text) {
                "&&" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.AND)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp4(ctx: WACCParser.BinaryOp4Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop4() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).text) {
                "==" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.EQUIV)
                "!=" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.NOTEQUIV)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp3(ctx: WACCParser.BinaryOp3Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop3() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).text) {
                ">" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.GT)
                ">=" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.GTE)
                "<" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.LT)
                "<=" -> node = BinaryOp(expr1, expr2, Boolean, BinaryOperator.LTE)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp2(ctx: WACCParser.BinaryOp2Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop2() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            when (ctx.getChild(1).text) {
                "+" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.PLUS)
                "-" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.MINUS)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

    override fun visitBinaryOp1(ctx: WACCParser.BinaryOp1Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr
        val expr2: Expr = visit(ctx.getChild(2)) as Expr

        val node: Identifier
        when (ctx.getChild(1).text) {
            "*" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.MULTI)
            "/" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.DIV)
            "%" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.MOD)
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
        val expr: Expr = visit(ctx.getChild(1)) as Expr

        val node: Identifier = when (ctx.getChild(0).text) {
            "!" -> UnaryOp(expr, Boolean, UnaryOperator.NOT)
            "-" -> UnaryOp(expr, Int, UnaryOperator.NEG)
            "len" -> UnaryOp(expr, Int, UnaryOperator.LEN)
            "ord" -> UnaryOp(expr, Int, UnaryOperator.ORD)
            "chr" -> UnaryOp(expr, Char, UnaryOperator.CHR)
            else -> throw Exception("Not Reachable")
        }
        return node
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(1))
    }

    //EXTENSION
    override fun visitAssignSideIf(ctx: WACCParser.AssignSideIfContext): Identifier {
        /*   0    1     2      3    4
            expr S_IF expr S_THEN expr
        */
        val cond = visit(ctx.getChild(0)) as Expr
        val assignIf = visit(ctx.getChild(2)) as Expr
        val assignElse = visit(ctx.getChild(4)) as Expr
        return SideIf(cond, assignIf, assignElse)
    }

    override fun visitIncrement(ctx: WACCParser.IncrementContext): Identifier {
        val lhs = visit(ctx.getChild(0)) as Expr

        return BinaryOp(lhs, IntLiteral("1"), Int, BinaryOperator.PLUS)
    }

    override fun visitDecrement(ctx: WACCParser.DecrementContext): Identifier {
        val lhs = visit(ctx.getChild(0)) as Expr

        return BinaryOp(lhs, IntLiteral("-1"), Int, BinaryOperator.PLUS)
    }

}
