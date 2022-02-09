package func

import stat.Stat
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
                System.err.println("Function $id already declared")
                valid = false
            } else if (returnType != t.returnType) {
                System.err.print("Expected " + t.returnType)
                System.err.println(", Got " + returnType)
                valid = false
            } else if (params.values.size != t.params.size) {
                System.err.print("Expected " + t.params.size)
                System.err.println(", Got " + params.values.size)
                valid = false
            } else {
                for (i in 0..(params.values.size - 1)) {
                    if (params.values[i].paramType != t.params[i]) {
                        System.err.print("Expected type " + t.params[i])
                        System.err.print(", Got type " + params.values[i].paramType)
                        valid = false
                    }
                }
            }
        }
    }
}
