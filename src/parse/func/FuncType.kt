package parse.func

import ErrorHandler
import ErrorType
import parse.semantics.SymbolTable
import parse.symbols.Type

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

    override fun getByteSize(): kotlin.Int = -1
}
