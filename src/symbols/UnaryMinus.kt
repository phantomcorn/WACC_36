package symbols

class UnaryMinus(val e: Expr) : UnaryOp(e, Int) {
    init {
        if (e.type != Int) {
            System.err.println("Type error in unary minus, expected: Int, got: " + e.type)
            valid = false;
        }
    }
}
