package parse.stat

import codegen.ASTVisitor
import parse.expr.Expr
import codegen.instr.Instruction
import parse.symbols.Array
import parse.symbols.Pair

class Free(val e: Expr) : Stat() {
    init {
        if (!(e.type is Pair || e.type is Array)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $e (expected: {Pair, Array}, actual: ${e.type})"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitFreeNode(this)
    }
}
