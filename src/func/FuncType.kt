package func

import symbols.Type
import visitor.SymbolTable

class FuncType(
    currentTable: SymbolTable,
    val id: String,
    val params: Array<Type?>,
) : Type() {
    var returnType: Type? = null
    init {
        if (currentTable.lookupAll(id) != null) {
            valid = false
        }
    }
}