package symbols

class Not(val e: Expr) : UnaryOp(e, Boolean) {
    init {
        if (e.type != Boolean) {
            System.err.println("Type error in not, expected: Boolean, got: " + e.type)
            valid = false;
        }
    }
}
