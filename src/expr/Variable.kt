package expr

import ErrorHandler
import stat.AssignLhs
import symbols.Type
import visitor.SymbolTable

class Variable(
    val text: String,
    st: SymbolTable<Type>
) : Expr(st.lookupAll(text)), AssignLhs {
    init {
        if (st.lookupAll(text) == null) {
            ErrorHandler.printErr(ErrorType.SEMANTIC, "Variable $text not defined in this scope")
        }
    }

    override fun toString(): String = text
}
