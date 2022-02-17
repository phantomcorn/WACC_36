package codegen
import expr.Expr
import instr.Instruction
import stat.AssignLhs
import stat.AssignRhs
import stat.Stat
import symbols.Type

interface ASTVisitor {

    /* Begin at root of AST. */

    fun visitAST()

    /* Code generation for statements. */

    fun visitSkipNode() : List<Instruction>

    fun visitWhileNode(cond : Expr, body : Stat) : List<Instruction>

    fun visitDeclarationNode(t : Type, id : String, rhs : AssignRhs) : List<Instruction>

    fun visitAssignmentNode(lhs : AssignLhs, rhs : AssignRhs) : List<Instruction>

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

    /* Code generation for types. */

    fun visitIntNode() : List<Instruction>

    fun visitBooleanNode() : List<Instruction>

    fun visitCharNode() : List<Instruction>

    fun visitStringNode() : List<Instruction>

    fun visitTypelessPairNode() : List<Instruction>

    fun visitPairInstanceNode() : List<Instruction>

    fun visitArgListNode() : List<Instruction>

    fun visitPairElemNode() : List<Instruction>

    fun visitArrayElemNode(id : String, values : Array<Expr>, dimension : Int) : List<Instruction>

    /* Code generation for binary operators. */

    fun visitOrNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitAndNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitEquivNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitNotEquivNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitGtNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitGteNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitLtNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitLteNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitPlusNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitMinusNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitMultiNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitDivNode(e1 : Expr, e2 : Expr) : List<Instruction>

    fun visitModNode(e1 : Expr, e2 : Expr) : List<Instruction>

    /* Code generation for unary operators. */

    fun visitNotNode(e : Expr) : List<Instruction>

    fun visitNegNode(e : Expr) : List<Instruction>

    fun visitLenNode(e : Expr) : List<Instruction>

    fun visitOrdNode(e : Expr) : List<Instruction>

    fun visitChrNode(e : Expr) : List<Instruction>

    /* Code generation for literals. */

    fun visitIntLiteralNode(token: kotlin.String) : List<Instruction>

    fun visitCharLiteralNode(token: kotlin.String) : List<Instruction>

    fun visitBooleanLiteralNode(token: kotlin.String) : List<Instruction>

    fun visitStringLiteralNode(token: kotlin.String) : List<Instruction>

    fun visitPairLiteralNode(token: kotlin.String) : List<Instruction>
}