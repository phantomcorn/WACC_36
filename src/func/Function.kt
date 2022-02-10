package func

import stat.Stat
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Function(
    currentTable: SymbolTable,
    val id: String,
    val returnType: Type?,
    val params: ParamList,
    val funcSymbolTable: SymbolTable,
    val body: Stat
) : Type() {
    init {
        val t = currentTable.lookup(id)
        if (t != null) {
            if (!(t is FuncType)) {
                //System.err.println("Function $id already declared")
                ErrorHandler.printErr(ErrorType.SEMANTIC, "\"$id\" is already defined in this scope")
                Identifier.valid = false
            } else if (returnType != t.returnType) {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at _ (expected: ${returnType.toString()} actual: _.toString")
                //System.err.print("Expected " + t.returnType)
                //System.err.println(", Got " + returnType)
                Identifier.valid = false
            } else if (params.values.size != t.params.size) {
                ErrorHandler.printErr(ErrorType.SEMANTIC, "Incorrect number of parameters for $id (expected: ${t.params.size} actual: ${params.values.size}")
                //System.err.print("Expected " + t.params.size)
                //System.err.println(", Got " + params.values.size)
                Identifier.valid = false
            } else {
                for (i in 0..(params.values.size - 1)) {
                    if (params.values[i].paramType != t.params[i]) {
                        //System.err.print("Expected type " + t.params[i])
                        //System.err.print(", Got type " + params.values[i].paramType)
                        ErrorHandler.printErr(ErrorType.SEMANTIC, "Incompatible type at ${params.values[i]} (expected: ${t.params[i]}, actual: ${params.values[i].paramType})")
                        Identifier.valid = false
                    }
                }
            }
        }
    }
}
