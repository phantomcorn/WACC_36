package symbols

class Lte(val e1: Expr, val e2: Expr) : BinaryOp(e1, e2, Boolean) {

    init {
        if (e1.type != e2.type) {
            System.err.println("Mismatching types " + e1.type + " and "+ e2.type)
            valid = false
        } else if ((e1.type != Int) && e1.type != Char) {
            System.err.println("Expected type Int or Char but actual type " + e1.type)
            valid = false
        }
    }
}
