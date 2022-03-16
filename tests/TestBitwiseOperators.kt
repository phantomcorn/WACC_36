package tests

import parse.symbols.Identifier
import parse.expr.BinaryOp
import parse.expr.BinaryOperator
import parse.expr.IntLiteral
import codegen.utils.ExprEvaluate

import codegen.WaccTreeVisitor
import codegen.ARMInstructionVisitor

class TestBitwiseOperators(): Test() {
    fun testBitwiseAnd {
        val lhs : Expr = IntLiteral("13")
        val rhs : Expr = IntLiteral("25")
        val expr = BinaryOp(lhs, rhs, parse.symbols.Int, BinaryOperator.BITWISE_AND)

        val intermediateCode = WaccTreeVisitor.visitAST(expr)
        ARMInstructionVisitor().visitInstructions(intermediateCode)

    }

    override fun test() {
        run(::testBitwiseAnd)
    }
}
