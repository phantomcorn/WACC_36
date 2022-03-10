package parse.stat

import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.symbols.Boolean
import parse.symbols.Type


class SideIf(val cond: Expr, val assignIf: Expr, val assignElse: Expr) : AssignRhs, ASTNode() {

    init {
        if (!(cond.type() is Boolean)) {
            ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incompatible type at $cond (expected: BOOL, actual: ${cond.type}"
            )
        }

        if (assignIf.type() != assignElse.type()) {
            ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Side expressions has two different types at $assignIf and $assignElse"
            )
        }
    }

    override fun type(): Type? {
        return assignIf.type()
    }


    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

}
