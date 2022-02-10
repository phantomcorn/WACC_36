package stat

import symbols.Identifier

class Assignment(val lhs: AssignLhs, val rhs: AssignRhs) : Stat() {
    init {
        if (lhs.type() != rhs.type()) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: ${lhs.type()}, actual: ${rhs.type()})"
            )
            Identifier.valid = false
        }
    }

    override fun toString() = "$lhs = $rhs"
}
