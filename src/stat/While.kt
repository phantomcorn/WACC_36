package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Boolean

class While(val e: Expr, val s: Stat) : Stat() {
    init {
        if (e.type !is Boolean) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: BOOL, actual: ${e.type})"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "while $e do $s done"
}
