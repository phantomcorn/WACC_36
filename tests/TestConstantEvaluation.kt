package tests

import parse.semantics.SymbolTable
import parse.symbols.Type
import parse.expr.Variable
import parse.symbols.Identifier
import parse.expr.BinaryOp
import parse.expr.BinaryOperator
import parse.expr.IntLiteral
import parse.expr.BooleanLiteral
import codegen.utils.ExprEvaluate

class TestConstantEvaluation(): Test() {
    fun addingLiteralsEvaluatesToLiteral() {
        val input = BinaryOp(IntLiteral("2"), IntLiteral("2"), parse.symbols.Int, BinaryOperator.PLUS)
        val output = ExprEvaluate.evaluate(input)
        assert(output is IntLiteral)
        assert((output as IntLiteral).value != null)
        assert(output.value!! == 4)
    }

    fun addingLiteralToVariableDoesNotReduce() {
        val st = SymbolTable<Type>(null) 
        st.add("x", parse.symbols.Int)
        val input = BinaryOp(IntLiteral("2"), Variable("x", st), parse.symbols.Int, BinaryOperator.PLUS)
        val output = ExprEvaluate.evaluate(input)
        assert(!(output is IntLiteral))
    }

    fun lessThanEvaluatesToLiteral() {
        val input = BinaryOp(IntLiteral("0"), IntLiteral("1"), parse.symbols.Int, BinaryOperator.LT)
        val output = ExprEvaluate.evaluate(input)
        assert(output is BooleanLiteral)
        assert((output as BooleanLiteral).value != null)
        assert(output.value!!)
    }

    override fun test() {
        run(::addingLiteralsEvaluatesToLiteral)
        run(::addingLiteralToVariableDoesNotReduce)
        run(::lessThanEvaluatesToLiteral)
    }
}
