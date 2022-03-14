package tests

import parse.symbols.Identifier
import parse.expr.BinaryOp
import parse.expr.BinaryOperator
import parse.expr.IntLiteral
import codegen.utils.ExprEvaluate

class TestConstantEvaluation(): Test() {
    fun testAddLiterals() {
        val input = BinaryOp(IntLiteral("2"), IntLiteral("2"), parse.symbols.Int, BinaryOperator.PLUS)
        val output = ExprEvaluate.evaluate(input)
        assert(output is IntLiteral)
        assert((output as IntLiteral).value != null)
        assert(output.value!! == 4)
    }

    override fun test() {
        run(::testAddLiterals)
    }
}
