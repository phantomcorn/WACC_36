package codegen
import expr.Expr
import instr.Instruction

interface ASTVisitor {

    fun visitAST()

    fun visitSkipNode() : List<Instruction>

    fun visitWhileNode() : List<Instruction>

    fun visitDeclarationNode() : List<Instruction>

    fun visitAssignmentNode() : List<Instruction>

    fun visitReadNode() : List<Instruction>

    fun visitExitNode() : List<Instruction>

    fun visitPrintNode() : List<Instruction>

    fun visitPrintlnNode() : List<Instruction>

    fun visitSemiNode() : List<Instruction>

    fun visitFreeNode() : List<Instruction>

    fun visitIfNode() : List<Instruction>

    fun visitBeginNode() : List<Instruction>

    fun visitReturnNode() : List<Instruction>

    fun visitVariableNode() : List<Instruction>

    fun visitNewPairNode() : List<Instruction>

    fun visitCallNode() : List<Instruction>

    fun visitArrayLiteralNode() : List<Instruction>

    fun visitEmptyArrayLiteralNode() : List<Instruction>

    fun visitArrayInstanceNode() : List<Instruction>

    fun visitIntNode() : List<Instruction>

    fun visitBooleanNode() : List<Instruction>

    fun visitCharNode() : List<Instruction>

    fun visitStringNode() : List<Instruction>

    fun visitTypelessPairNode() : List<Instruction>

    fun visitPairInstanceNode() : List<Instruction>

    fun visitArgListNode() : List<Instruction>

    fun visitPairElemNode() : List<Instruction>

    fun visitArrayElemNode(id : String, values : Array<Expr>, dimension : Int) : List<Instruction>

    fun visitPairLiteral() : List<Instruction>

    fun visitOrNode() : List<Instruction>

    fun visitAndNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitEquivNode() : List<Instruction>

    fun visitNotEquivNode() : List<Instruction>

    fun visitGtNode() : List<Instruction>

    fun visitGteNode() : List<Instruction>

    fun visitLtNode() : List<Instruction>

    fun visitLteNode() : List<Instruction>

    fun visitPlusNode() : List<Instruction>

    fun visitMinusNode() : List<Instruction>

    fun visitMultiNode() : List<Instruction>

    fun visitDivNode() : List<Instruction>

    fun visitModNode() : List<Instruction>

    fun visitIntLiteralNode() : List<Instruction>

    fun visitCharLiteralNode() : List<Instruction>

    fun visitBooleanLiteralNode() : List<Instruction>

    fun visitStringLiteralNode() : List<Instruction>

    fun visitNotNode(e : Expr) : List<Instruction>

    fun visitNegNode(e : Expr) : List<Instruction>

    fun visitLenNode(e : Expr) : List<Instruction>

    fun visitOrdNode(e : Expr) : List<Instruction>

    fun visitChrNode(e : Expr) : List<Instruction>
}