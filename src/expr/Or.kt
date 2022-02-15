package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Boolean

class Or(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Boolean) {

    init {
        if (e1.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: BOOL, actual: " + e1.type + ")")
        } else if (e2.type != Boolean) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: BOOL, actual: " + e2.type + ")")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString() : String =
        "$e1||$e2"
}
