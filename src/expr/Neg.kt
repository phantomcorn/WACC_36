package expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import symbols.Int

class Neg(e: Expr) : UnaryOp(e, Int) {

    init {
        if (e.type != Int) {
            System.err.println("Expected type int but actual type " + e.type)
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitNegNode(e)
    }
}
