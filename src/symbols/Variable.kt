package symbols

import myvisitor.SymbolTable

class Variable(
    val text: kotlin.String,
    st: SymbolTable
) : Expr((st.lookupAll(text) as Expr?)?.type) {
    init {
        if (st.lookupAll(text) == null) {
            System.err.println("Identifier: " + text + " accessed before definition")
            valid = false
        }
    }
}
