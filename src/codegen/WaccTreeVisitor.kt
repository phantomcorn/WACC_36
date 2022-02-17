package codegen

import expr.Expr
import instr.Instruction
import stat.AssignLhs
import stat.AssignRhs
import stat.Stat
import symbols.Type

class WaccTreeVisitor : ASTVisitor {

    /* Begin at root of AST. */

    override fun visitAST() {
        TODO("Not yet implemented")
    }

    /* Code generation for statements. */

    override fun visitSkipNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitWhileNode(cond : Expr, body : Stat): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitDeclarationNode(t : Type, id : String, rhs : AssignRhs): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitAssignmentNode(lhs : AssignLhs, rhs : AssignRhs): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitReadNode(lhs : AssignLhs): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitExitNode(e : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintNode(e : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintlnNode(e : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitSemiNode(s1 : Stat, s2 : Stat): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitFreeNode(e : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(cond : Expr, thenBody : Stat, elseBody : Stat): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBeginNode(body : Stat): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitReturnNode(e : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitVariableNode(id : String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitNewPairNode(fst : Expr, snd : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCallNode(values : Array<Expr>, funcName : String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitEmptyArrayLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    /*
    override fun visitArrayInstanceNode(): List<Instruction> {
        TODO("Not yet implemented")
    }
    */

    /* Code generation for types. */

    override fun visitIntNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitTypelessPairNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairInstanceNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArgListNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairElemNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayElemNode(id: String, values: Array<Expr>, dimension: Int): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for binary operators. */

    override fun visitOrNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitAndNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitEquivNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitNotEquivNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitGtNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitGteNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitLtNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitLteNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPlusNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitMinusNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitMultiNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitDivNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitModNode(e1 : Expr, e2 : Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for literals. */

    override fun visitIntLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for unary operators. */

    override fun visitNotNode(e: Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitNegNode(e: Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitLenNode(e: Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitOrdNode(e: Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitChrNode(e: Expr): List<Instruction> {
        TODO("Not yet implemented")
    }

}