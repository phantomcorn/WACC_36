package stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Type

class Return(val e: Expr, t: Type?) : Stat() {
    init {
        if (e.type != t) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: $t, actual: ${e.type})"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitReturnNode(e)
    }
}
