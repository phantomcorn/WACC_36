package stat

class Assignment(val lhs: AssignLhs, val rhs: AssignRhs) : Stat() {
    init {
        if (lhs.type() != rhs.type()) {
            System.err.println("Expected " + lhs.type() + ", got " + rhs.type())
            valid = false
        }
    }
}
