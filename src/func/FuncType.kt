package func

import symbols.Type
import visitor.SymbolTable

class FuncType(
    currentTable: SymbolTable<Function>,
    val id: String,
    val params: Array<Type?>,
    val returnType: Type?
) : Type(), Function {
    init {
        if (currentTable.lookupAll(id) != null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "\"$id\" already defined in this scope"
            )
        }
    }
}
