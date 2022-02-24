package codegen

import codegen.instr.Instruction
import parse.expr.ArrayElem
import parse.expr.BinaryOp
import parse.expr.UnaryOp
import parse.expr.Variable
import parse.stat.*


interface ASTVisitor {

    /* Begin at root of AST. */

    fun visitAST(root: ASTNode): List<Instruction>

    /* Code generation for statements. */

    fun visitSkipNode(): List<Instruction>

    fun visitWhileNode(node: While): List<Instruction>

    fun visitDeclarationNode(node: Declaration): List<Instruction>

    fun visitAssignmentNode(node: Assignment): List<Instruction>

    fun visitReadNode(node: Read): List<Instruction>

    fun visitExitNode(node: Exit): List<Instruction>

    fun visitPrintNode(node: Print): List<Instruction>

    fun visitPrintlnNode(node: Println): List<Instruction>

    fun visitStatListNode(statList: StatList): List<Instruction>

    fun visitFreeNode(node: Free): List<Instruction>

    fun visitIfNode(node: If): List<Instruction>

    fun visitBeginNode(node: Begin): List<Instruction>

    fun visitReturnNode(node: Return): List<Instruction>

    fun visitVariableNode(node: Variable): List<Instruction>

    fun visitNewPairNode(node: NewPair): List<Instruction>

    fun visitCallNode(node: Call): List<Instruction>

    fun visitArrayLiteralNode(node: ArrayLiteral): List<Instruction>

    fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral): List<Instruction>

    //fun visitArrayInstanceNode() : List<Instruction>

    /* Code generation for types. */

    fun visitIntNode(): List<Instruction>

    fun visitBooleanNode(): List<Instruction>

    fun visitCharNode(): List<Instruction>

    fun visitStringNode(): List<Instruction>

    fun visitTypelessPairNode(): List<Instruction>

    fun visitPairInstanceNode(): List<Instruction>

    fun visitArgListNode(): List<Instruction>

    fun visitPairElemNode(): List<Instruction>

    fun visitArrayElemNode(node: ArrayElem): List<Instruction>

    /* Code generation for binary operators. */

    fun visitBinaryOp(node: BinaryOp): List<Instruction>

    /* Code generation for unary operators. */

    fun visitUnaryOpNode(node: UnaryOp): List<Instruction>

    /* Code generation for literals. */

    fun visitIntLiteralNode(node: IntLiteral): List<Instruction>

    fun visitCharLiteralNode(node: CharLiteral): List<Instruction>

    fun visitBooleanLiteralNode(node: BooleanLiteral): List<Instruction>

    fun visitStringLiteralNode(node: StringLiteral): List<Instruction>

    fun visitPairLiteralNode(node: PairLiteral): List<Instruction>

}
