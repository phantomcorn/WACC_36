package stat

class Assignment(val lhs: AssignLhs, val rhs: AssignRhs) : Stat() {
    init {
        if (lhs.type() != rhs.type()) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: ${lhs.type()}, actual: ${rhs.type()})"
            )
        }
    }

    override fun toString() = "$lhs = $rhs"
}
