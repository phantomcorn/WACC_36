package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.expr.BinaryOperator
import parse.expr.Expr
import parse.symbols.Int
import parse.symbols.Type



class SideEffectOp(val lhs: AssignLhs, val incrAmount: Expr, val op : BinaryOperator) : Stat(), AssignLhs  {

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
        return v.visitSideEffectOp(this)
    }

    override fun type(): Type {
        return Int
    }

    override fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable> {
        return v.visitSideEffectOpLhs(this)
    }


}