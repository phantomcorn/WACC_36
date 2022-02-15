package func

import codegen.ASTNode
import codegen.ASTVisitor
import instr.Instruction
import stat.Stat
import symbols.Type
import visitor.SymbolTable

class FuncAST(
    currentTable: SymbolTable<Function>,
    id: String,
    val returnType: Type?,
    val params: ParamList,
    val body: Stat
) : ASTNode(), Function {
    init {
        val t = currentTable.lookup(id)
        if (t != null) {
            if (!(t is FuncType)) {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "\"$id\" is already defined in this scope")
            } else if (returnType != t.returnType) {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible return types (expected: ${returnType.toString()} actual: ${t.returnType}")
            } else if (params.values.size != t.params.size) {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Incorrect number of parameters for $id (expected: ${t.params.size} actual: ${params.values.size}")
            } else {
                for (i in 0..(params.values.size - 1)) {
                    if (params.values[i].paramType != t.params[i]) {
                        ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${params.values[i]} (expected: ${t.params[i]}, actual: ${params.values[i].paramType})")
                    }
                }
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }
}
