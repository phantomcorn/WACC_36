package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.symbols.Type

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
        return v.visitReturnNode(this)
    }
}
