package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Boolean

class Not(val e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at $e (expected: BOOLEAN, actual ${e.type})")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitNotNode(e)
    }
}
