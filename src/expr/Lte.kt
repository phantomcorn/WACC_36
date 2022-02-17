package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Boolean
import symbols.Char
import symbols.Int

class Lte(e1: Expr, e2: Expr) : BinaryOp(e1, e2, Boolean) {

    init {
        if (e1.type != e2.type) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Mismatched expression types at $this with + ${e1.type} and ${e2.type})")
        } else if ((e1.type != Int) && e1.type != Char) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at " + this.toString() + " (expected: {INT, CHAR}, actual: " + e1.type + ")")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitLteNode(e1,e2)
    }

    override fun toString() =
        "$e1<=$e2"
}
