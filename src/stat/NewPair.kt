package stat

import codegen.ASTNode
import codegen.ASTVisitor
import expr.Expr
import instr.Instruction
import symbols.Identifier
import symbols.Pair
import symbols.Type
import symbols.TypelessPair

class NewPair(val e1: Expr, val e2: Expr) : ASTNode(), AssignRhs {
    val type: Pair
    init {
        var t1 = e1.type()
        if (t1 is Pair) {
            t1 = TypelessPair
        }
        var t2 = e2.type()
        if (t2 is Pair) {
            t2 = TypelessPair
        }
        type = symbols.PairInstance(t1, t2)
    }

    override fun type(): Type = type

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitNewPairNode(this)
    }

    override fun toString(): String = "newpair($e1, $e2)"

}
