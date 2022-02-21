package codegen
import expr.ArrayElem
import expr.Expr
import expr.Variable
import instr.Instruction
import stat.*
import symbols.Type

interface ASTVisitor {

    /* Begin at root of AST. */

    fun visitAST()

    /* Code generation for statements. */

    fun visitSkipNode() : List<Instruction>

    fun visitWhileNode(node: While) : List<Instruction>

    fun visitDeclarationNode(node: Declaration) : List<Instruction>

    fun visitAssignmentNode(node: Assignment) : List<Instruction>

    fun visitReadNode(node: Read) : List<Instruction>

    fun visitExitNode(node: Exit) : List<Instruction>

    fun visitPrintNode(node: Print) : List<Instruction>

    fun visitPrintlnNode(node: Println) : List<Instruction>

    fun visitStatListNode(statList: StatList) : List<Instruction>

    fun visitFreeNode(node: Free) : List<Instruction>

    fun visitIfNode(node: If) : List<Instruction>

    fun visitBeginNode(node: Begin) : List<Instruction>

    fun visitReturnNode(node: Return) : List<Instruction>

    fun visitVariableNode(node: Variable) : List<Instruction>

    fun visitNewPairNode(node: NewPair) : List<Instruction>

    fun visitCallNode(node: Call) : List<Instruction>

    fun visitArrayLiteralNode(node: ArrayLiteral) : List<Instruction>

    fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral) : List<Instruction>

    //fun visitArrayInstanceNode() : List<Instruction>

    /* Code generation for types. */

    fun visitIntNode() : List<Instruction>

    fun visitBooleanNode() : List<Instruction>

    fun visitCharNode() : List<Instruction>

    fun visitStringNode() : List<Instruction>

    fun visitTypelessPairNode() : List<Instruction>

    fun visitPairInstanceNode() : List<Instruction>

    fun visitArgListNode() : List<Instruction>

    fun visitPairElemNode() : List<Instruction>

    fun visitArrayElemNode(node: ArrayElem) : List<Instruction>

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
