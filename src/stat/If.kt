package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Boolean

class If(val e: Expr, val s1: Stat, val s2: Stat) : Stat() {
    init {
        if (!(e.type is Boolean)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: BOOL, actual: ${e.type}"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitIfNode(e, s1, s2)
    }
}
