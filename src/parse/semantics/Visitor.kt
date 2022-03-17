package parse.semantics

import ErrorHandler
import ErrorType
import antlr.WACCParser
import antlr.WACCParserBaseVisitor
import parse.expr.*
import parse.func.*
import parse.func.Function
import parse.sideeffect.Index
import parse.sideeffect.SideIf
import parse.sideeffect.SideEffectExpr
import parse.stat.*
import parse.symbols.*
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int
import codegen.utils.ExprEvaluate
import parse.symbols.Array

class Visitor : WACCParserBaseVisitor<Identifier>() {

    var functionST: SymbolTable<Function> = SymbolTable(null)
    var currentSymbolTable: SymbolTable<Type> = SymbolTable(null)
    var functionTypeMap: MutableMap<String, MutableList<Function>> = HashMap()

    override fun visitProg(ctx: WACCParser.ProgContext): Identifier? {
        ErrorHandler.setContext(ctx)

        /* PARSER RULE prog : BEGIN  parse.func* parse.stat  END EOF;

                              0     1    2   3
        prog with 0 func :  BEGIN stat  END EOF;

                      last func at index : 0

                              0     1    2   3   4
                  1 func :  BEGIN func stat END EOF;

                      last func at index : 1

                              0    1..n   n+1  n+2 n+3
                  n func :  BEGIN  func   stat END EOF;

                       last parse.func at index : n


         */
        val lastFuncIndex = ctx.childCount - 4
        //1st pass : iterate through each function declaration and add temporary placeholder for them
        for (i in 1..lastFuncIndex) {
            val childFuncContext = ctx.getChild(i)
            var offset = 0
            if (childFuncContext is WACCParser.FuncContext) {
                val funcName = childFuncContext.IDENT().text
                val funcType = visit(childFuncContext.type()) as Type?
                val types = mutableListOf<Type?>()
                val varNames = mutableListOf<String>()
                var generic = false
                if (childFuncContext.param_list() != null) {
                    //in each function, iterate through its parameter
                    for (j in 0 until childFuncContext.param_list().param().size) {
                        val param = childFuncContext.param_list().param()[j]
                        val paramType = visit(param.param_type()) as Type?
                        types.add(paramType)
                        varNames.add(param.IDENT().text)
                        if (paramType is Generic) {
                            generic = true
                        }
                    }
                    offset = 1
                }
                val ft = FuncType(functionST, funcName, types.toTypedArray(), varNames.toTypedArray(), funcType, childFuncContext.getChild(5 + offset), childFuncContext, generic)
                if (functionTypeMap.contains(funcName)) {
                    functionTypeMap[funcName]!!.add(ft)
                } else {
                    functionTypeMap[funcName] = mutableListOf(ft)
                }
                functionST.add(ft.id, ft)
            }
        }

        /* 2nd pass : visit all the function and create actual function node for it in the function symbol table
           - to solve calling a function within a function problem
        */

        val statBody = ctx.getChild(ctx.childCount - 3)
        //visit the main body of the program
        return visit(statBody)
    }

    override fun visitParam_type(ctx: WACCParser.Param_typeContext): Identifier {
        return if (ctx.GENERIC_DEC() != null) {
            Generic
        } else {
            visit(ctx.type())
        }
    }

    fun visitFuncAST(funcType: FuncType): Identifier? {
        ErrorHandler.setContext(funcType.funcCtx)
        funcType.visited = true
        val prevST = currentSymbolTable
        val funcSymbolTable = SymbolTable<Type>(null)
        currentSymbolTable = funcSymbolTable
        if (funcType.returnType != null) {
            currentSymbolTable.add("$", funcType.returnType)
        }

        val paramList = mutableListOf<Parameter>()
        for (i in 0 until funcType.params.size) {
            if (funcType.params[i] != null) {
                val param: Parameter = Parameter(funcType.params[i], funcType.paramIds[i])
                paramList.add(param)
                currentSymbolTable.add(param.paramName, param.paramType!!)
            }
        }

        functionST.add(funcType.id, funcType)
        functionTypeMap[funcType.id.substring(0, funcType.id.indexOf('$'))]!!.add(funcType)

        val funcBody: Stat = visit(funcType.body) as Stat
        if (!ReturnChecker.check(funcBody)) {
            ErrorHandler.printErr(
                    ErrorType.SYNTAX,
                    "Function ${funcType.id} is not ended with a return or an exit statement"
            )
        }
        currentSymbolTable = prevST

        val funcParam = ParamList(paramList)

        val funcAST = FuncAST(functionST, funcType.id, funcType.returnType, funcParam, funcBody, funcSymbolTable)

        functionST.add(funcType.id, funcAST)
        val funcList = functionTypeMap[funcType.id.substring(0, funcType.id.indexOf('$'))]!!
        for (i in 0 until funcList.size) {
            if (funcList[i] == funcType) {
                funcList[i] = funcAST
                break
            }
        }

        return funcAST
    }

    //Functions
    override fun visitFunc(ctx: WACCParser.FuncContext): Identifier? {
        return null
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
        val sb = StringBuilder(ctx.IDENT().text)
        sb.append("$")
        for (value in values) {
            if (value.type() != null) {
                sb.append("_")
                sb.append(value.type()!!.toArg())
            }
        }
        val funcName = sb.toString()
        var func = functionST.lookup(funcName)
        if (func == null) {
            if (functionTypeMap[ctx.IDENT().text] != null) {
                outer@ for (possibleFunc in functionTypeMap[ctx.IDENT().text]!!) {
                    if (possibleFunc !is FuncType) {
                        continue
                    }
                    if (values.size != possibleFunc.params.size) {
                        continue
                    }
                    for (i in 0 until possibleFunc.params.size) {
                        if (possibleFunc.params[i] !is Generic && values[i].type != possibleFunc.params[i]) {
                            continue@outer
                        }
                    }
                    func = possibleFunc
                }
            }
            println("Test")
            if (func is FuncType && !func.visited) {
                val newParams = mutableListOf<Type?>()
                for (i in 0 until func.params.size) {
                    if (func.params[i] is Generic) {
                        if (values[i].type is Pair) {
                            newParams.add(TypelessPair)
                        } else if (values[i].type is Array) {
                            newParams.add(TypelessArray)
                        } else {
                            newParams.add(values[i].type)
                        }
                    } else {
                        newParams.add(func.params[i])
                    }
                }
                func = FuncType(functionST, funcName, newParams.toTypedArray(), func.paramIds, func.returnType, func.body, func.funcCtx, false)
            } else {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Function ${ctx.IDENT().text} is not defined in this scope.")
            }
        }
        if (func is FuncType && !func.visited) {
            visitFuncAST(func)
        }
        return Call(values, funcName, functionST)
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

    override fun visitBinaryOp7(ctx: WACCParser.BinaryOp7Context): Identifier? {
        ErrorHandler.setContext(ctx)
        val expr1: Expr = visit(ctx.getChild(0)) as Expr

        var node: Identifier = expr1
        if (ctx.binop7() != null) {
            val expr2: Expr = visit(ctx.getChild(2)) as Expr

            node = when (ctx.getChild(1).text) {
                "&" -> BinaryOp(expr1, expr2, Int, BinaryOperator.BITWISE_AND)
                "|" -> BinaryOp(expr1, expr2, Int, BinaryOperator.BITWISE_OR)
                "^" -> BinaryOp(expr1, expr2, Int, BinaryOperator.BITWISE_XOR)
                else -> throw Exception("Not Reachable")
            }
        }
        return node
    }

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
        return ExprEvaluate.evaluate(node)
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
        return ExprEvaluate.evaluate(node)
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
        return ExprEvaluate.evaluate(node)
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
                "<<" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.LOGICAL_SHIFT_LEFT)
                ">>" -> node = BinaryOp(expr1, expr2, Int, BinaryOperator.LOGICAL_SHIFT_RIGHT)
                else -> throw Exception("Not Reachable")
            }
        }
        return ExprEvaluate.evaluate(node)
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
        return ExprEvaluate.evaluate(node)
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
        return ExprEvaluate.evaluate(node)
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
            "~" -> UnaryOp(expr, Int, UnaryOperator.BITWISE_NOT)
            else -> throw Exception("Not Reachable")
        }
        return ExprEvaluate.evaluate(node)
    }

    override fun visitParens(ctx: WACCParser.ParensContext): Identifier? {
        ErrorHandler.setContext(ctx)
        return visit(ctx.getChild(1))
    }

    //EXTENSION

    override fun visitIncrementDecrement(ctx: WACCParser.IncrementDecrementContext): Identifier {
        return visit(ctx.getChild(0))
    }


    override fun visitAssignSideIf(ctx: WACCParser.AssignSideIfContext): Identifier {
        /*   0    1     2      3    4
            expr S_IF expr S_THEN expr
        */
        val cond = visit(ctx.getChild(0)) as Expr
        val assignIf = visit(ctx.getChild(2)) as Expr
        val assignElse = visit(ctx.getChild(4)) as Expr
        return SideIf(cond, assignIf, assignElse)
    }

    override fun visitPostIncrDecr(ctx: WACCParser.PostIncrDecrContext): Identifier {
        /*      0         1
            assign_lhs incrDecr
        */
        val one = IntLiteral("1")
        val lhs = visit(ctx.getChild(0)) as AssignLhs
        val node: Identifier = when (ctx.getChild(1).text) {
            "++" -> SideEffectExpr(lhs, one, BinaryOperator.PLUS, Index.POST)
            "--" -> SideEffectExpr(lhs , one, BinaryOperator.MINUS, Index.POST)
            else -> throw Exception("Not Reachable")
        }
        return node

    }

    override fun visitPreIncrDecr(ctx: WACCParser.PreIncrDecrContext): Identifier {
        /*      0        1
            incrDecr assign_lhs
        */
        val one = IntLiteral("1")
        val lhs = visit(ctx.getChild(1)) as AssignLhs
        val node: Identifier = when (ctx.getChild(0).text) {
            "++" -> SideEffectExpr(lhs, one, BinaryOperator.PLUS, Index.PRE)
            "--" -> SideEffectExpr(lhs , one, BinaryOperator.MINUS, Index.PRE)
            else -> throw Exception("Not Reachable")
        }
        return node
    }

    override fun visitArithSideEffect(ctx: WACCParser.ArithSideEffectContext): Identifier {
        return visit(ctx.getChild(0))
    }

    override fun visitArithmeticSideEffect(ctx: WACCParser.ArithmeticSideEffectContext): Identifier {
        /*       0       1      2     3
            assign_lhs binop2 EQUALS expr

        */

        val e = visit(ctx.getChild(3)) as Expr
        val lhs = visit(ctx.getChild(0)) as AssignLhs
        val node: Identifier = when (ctx.getChild(1).text) {
            "+" -> SideEffectExpr(lhs, e, BinaryOperator.PLUS, Index.POST)
            "-" -> SideEffectExpr(lhs , e, BinaryOperator.MINUS, Index.POST)
            "*" -> SideEffectExpr(lhs , e, BinaryOperator.MULTI, Index.POST)
            "/" -> SideEffectExpr(lhs , e, BinaryOperator.DIV, Index.POST)
            "%" -> SideEffectExpr(lhs , e, BinaryOperator.MOD, Index.POST)
            else -> throw Exception("Not Reachable")
        }

        return node

    }



    // assignment to Increment and Decrement
    override fun visitIncrementDecrementRhs(ctx: WACCParser.IncrementDecrementRhsContext): Identifier {
        return visit(ctx.getChild(0))
    }
}
