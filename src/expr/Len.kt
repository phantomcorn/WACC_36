package expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import symbols.Array
import symbols.Int

class Len(e: Expr) : UnaryOp(e, Int) {
    init {
        if (!(e.type is Array)) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${this.toString()} (expected: ARRAY, actual ${e.type}")
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitLenNode(e)
    }
}
