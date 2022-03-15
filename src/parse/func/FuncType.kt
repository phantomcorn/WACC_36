package parse.func

import ErrorHandler
import ErrorType
import parse.semantics.SymbolTable
import parse.symbols.Type

class FuncType(
    currentTable: SymbolTable<Function>,
    var id: String,
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
        val sb = StringBuilder(id)
        for (param in params) {
            if (param != null) {
                sb.append("_")
                sb.append(param.toString())
            }
        }
        id = sb.toString()
    }

    override fun getByteSize(): kotlin.Int = -1
}
