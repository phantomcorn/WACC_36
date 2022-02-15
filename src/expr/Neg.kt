package expr

import codegen.ASTVisitor
import instr.Instruction
import symbols.Int

class Neg(val e: Expr) : UnaryOp(e, Int) {

    init {
        if (e.type != Int) {
            System.err.println("Expected type int but actual type " + e.type)
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitNegNode(e)
    }
}
