package parse.func

import ErrorHandler
import ErrorType
import antlr.WACCParser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.ParseTree
import parse.semantics.SymbolTable
import parse.symbols.Type

class FuncType(
    currentTable: SymbolTable<Function>,
    var id: String,
    val params: Array<Type?>,
    val paramIds: Array<String>,
    val returnType: Type?,
    val body: ParseTree,
    val funcCtx: ParserRuleContext,
    val generic: Boolean
) : Type(), Function {
    var visited: kotlin.Boolean = false
    init {
        val sb = StringBuilder(id)
        sb.append("$")
        for (param in params) {
            if (param != null) {
                sb.append("_")
                sb.append(param.toArg())
            }
        }
        id = sb.toString()
        if (currentTable.lookupAll(id) != null) {
            ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "\"$id\" already defined in this scope"
            )
        }
    }

    override fun getByteSize(): kotlin.Int = -1

    override fun toString(): String = id
}
