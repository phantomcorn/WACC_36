package expr

import myvisitor.SymbolTable
import stat.AssignLhs

class Variable(
    val text: kotlin.String,
    st: SymbolTable
) : Expr((st.lookupAll(text) as Expr?)?.type), AssignLhs {
    init {
        if (st.lookupAll(text) == null) {
            System.err.println("Identifier: " + text + " accessed before definition")
            valid = false
        }
    }
}
