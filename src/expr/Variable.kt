package expr

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
            System.err.println("Identifier: " + text + " accessed before definition")
            Identifier.valid = false
        }
    }
}
