package parse.sideeffect

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.expr.Expr
import parse.stat.AssignLhs
import parse.stat.AssignRhs

class MultiAssignment(lhs : AssignLhs, other : Array<Expr>, rhs : AssignRhs) : Expr(lhs.type(), other.sumOf { x -> x.weight }){
    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }
}