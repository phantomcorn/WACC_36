package parse.stat

import codegen.ASTVisitor
import parse.expr.Expr
import codegen.instr.Instruction
import parse.symbols.Int

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
        return v.visitExitNode(this)
    }
}
