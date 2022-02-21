package expr

import ErrorHandler
import codegen.ASTVisitor
import codegen.instr.Instruction
import symbols.Boolean

class Equiv(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Boolean) {

    init {
        if (e1.type != e2.type) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Mismatched expression types at $this with + ${e1.type} and ${e2.type})")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitEquivNode(e1,e2)
    }

    override fun toString(): String =
        "$e1==$e2"
}
