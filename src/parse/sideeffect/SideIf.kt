package parse.sideeffect

import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.stat.AssignRhs
import parse.symbols.Boolean
import parse.symbols.Type


class SideIf(val cond: Expr, val exprIfTrue: Expr, val exprIfFalse: Expr) : AssignRhs, ASTNode() {

    val id : String = ""

    init {
        if (!(cond.type() is Boolean)) {
            ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incompatible type at $cond (expected: BOOL, actual: ${cond.type}"
            )
        }

        if (exprIfTrue.type() != exprIfFalse.type()) {
            ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Side expressions has two different types at $exprIfTrue and $exprIfFalse"
            )
        }
    }

    override fun type(): Type? {
        return exprIfTrue.type()
    }


    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitAssignSideIf(this)
    }

}
