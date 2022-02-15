package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Char
import symbols.Int

class Chr(val e: Expr) : UnaryOp(e, Char) {
    init {
        if (e.type != Int) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this} (expected: INT, actual ${e.type})")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitChrNode(e)
    }
}
