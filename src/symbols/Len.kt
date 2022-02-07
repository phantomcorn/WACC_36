package symbols

class Len(val e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != String) {
            System.err.println("Type error in unary operator len expected: String, got: " + e.type)
            valid = false
        }
    }
}
