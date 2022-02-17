package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Int

class Exit(val e: Expr) : Stat() {
    init {
        if (e.type !is Int) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: Int, actual: " + e.type + ")"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitExitNode(e)
    }
}
