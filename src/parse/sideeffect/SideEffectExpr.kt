package parse.sideeffect

import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.expr.BinaryOperator
import parse.expr.Expr
import parse.stat.AssignLhs
import parse.stat.AssignRhs
import parse.symbols.Int
import java.lang.Exception


class SideEffectExpr(
    val lhs: AssignLhs,
    val e: Expr,
    val op : BinaryOperator,
    val index : Index)
    : Expr(Int, e.weight), AssignLhs, AssignRhs
{

    init {
        if (!(lhs.type() is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $lhs (expected: Int, actual: ${lhs.type()}"
            )
        }

        if (!(e.type() is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: Int, actual: ${e.type()}"
            )
        }

        if (!(op == BinaryOperator.PLUS
                    || op == BinaryOperator.MINUS
                    || op == BinaryOperator.MOD
                    || op == BinaryOperator.MULTI
                    || op == BinaryOperator.DIV)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible side effect expression " +
                        "(expected: ${BinaryOperator.PLUS}, ${BinaryOperator.MINUS} actual: $op)"
            )
        }


    }


    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitSideEffectExpr(this)
    }

    override fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable> {
        TODO("Not yet implemented")
    }


    override fun toString(): String {
        val op = when (op) {
            BinaryOperator.PLUS -> "++"
            BinaryOperator.MINUS -> "--"
            else -> throw Exception("Unreachable")
        }

        return when (index) {
            Index.POST -> "$lhs$op"
            Index.PRE -> "$op$lhs"
        }
    }


}