package parse.stat

import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.symbols.Pair
import parse.symbols.Type
import parse.symbols.TypelessPair

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
        type = parse.symbols.PairInstance(t1, t2)
    }

    override fun type(): Type = type

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitNewPairNode(this)
    }

    override fun toString(): String = "newpair($e1, $e2)"

}
