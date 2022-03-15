package parse.sideeffect

import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.expr.BinaryOperator
import parse.expr.Expr
import parse.stat.AssignLhs
import parse.stat.AssignRhs
import parse.stat.Stat
import parse.symbols.Int
import java.lang.Exception


class SideEffectExpr(
    val lhs: AssignLhs,
    val incrAmount: Expr,
    val op : BinaryOperator,
    val index : Index)
    : Expr(Int, incrAmount.weight), AssignLhs, AssignRhs
{

    init {
        if (!(lhs.type() is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $lhs (expected: Int, actual: ${lhs.type()}"
            )
        }

        if (!(incrAmount.type() is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $incrAmount (expected: Int, actual: ${incrAmount.type()}"
            )
        }

        if (!(op == BinaryOperator.PLUS || op == BinaryOperator.MINUS)) {
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