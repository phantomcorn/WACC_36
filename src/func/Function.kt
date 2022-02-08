package func

import visitor.SymbolTable
import symbols.Type
import kotlin.Array

class Function(
    currentTable: SymbolTable,
    val id: String,
    val returnType: Type,
    val params: Array<Parameter>,
    val symbolTable: SymbolTable
) : Type() {
    init {
        if (currentTable.lookupAll(id) != null){
            valid = false
        }
    }
}
