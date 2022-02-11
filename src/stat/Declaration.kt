package stat

import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Declaration(
    val t: Type,
    val id: kotlin.String,
    val rhs: AssignRhs,
    st: SymbolTable
) : Stat() {
    init {
        if (rhs.type() != t) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: $t, actual: " + rhs.type() + ")"
            )
            Identifier.valid = false
        } else if (st.lookup(id) != null) {
            System.err.println("\"$id\" is already defined in this scope")
            Identifier.valid = false
        } else {
            st.add(id, t)
        }
    }

    override fun toString(): String {
        return "$t $id = $rhs"
    }
}
