package codegen

interface ASTNode {
    fun accept(v: ASTVisitor): List<Instruction>
}
