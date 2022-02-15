package stat

import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Array
import symbols.Pair

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
        TODO("Not yet implemented")
    }
}
