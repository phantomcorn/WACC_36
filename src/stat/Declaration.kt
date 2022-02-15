package stat

import symbols.Type
import visitor.SymbolTable

class Declaration(
    val t: Type,
    val id: String,
    val rhs: AssignRhs,
    st: SymbolTable<Type>
) : Stat() {
    init {
        if (rhs.type() != t) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: $t, actual: " + rhs.type() + ")"
            )
        } else if (st.lookup(id) != null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "\"$id\" is already defined in this scope"
            )
        } else {
            st.add(id, t)
        }
    }

    override fun toString(): String {
        return "$t $id = $rhs"
    }
}
