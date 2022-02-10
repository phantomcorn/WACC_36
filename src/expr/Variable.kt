package expr

import ErrorHandler
import stat.AssignLhs
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Variable(
    val text: kotlin.String,
    st: SymbolTable
) : Expr(st.lookupAll(text) as Type?), AssignLhs {
    init {
        if (st.lookupAll(text) == null) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Variable $text not defined in this scope")
            Identifier.valid = false
        }
    }
}
