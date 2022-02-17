package codegen

import expr.Expr
import instr.Instruction
import stat.AssignLhs
import stat.AssignRhs
import stat.Stat
import symbols.Type

class WaccTreeVisitor : ASTVisitor {

    override fun visitAST() {
        TODO("Not yet implemented")
    }

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

    override fun visitReadNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitExitNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintlnNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitSemiNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitFreeNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBeginNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitReturnNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitVariableNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitNewPairNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCallNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitEmptyArrayLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayInstanceNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

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

    override fun visitPairLiteral(): List<Instruction> {
        TODO("Not yet implemented")
    }

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

    override fun visitIntLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteralNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

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