package func

import stat.Stat
import visitor.SymbolTable
import symbols.Type
import kotlin.Array

class Function(
    currentTable: SymbolTable,
    val id: String,
    val returnType: Type,
    val params: ParamList,
    val funcSymbolTable: SymbolTable,
    val body: Stat
) : Type() {
    init {
        if (currentTable.lookupAll(id) != null){
            valid = false
        }
    }
}
