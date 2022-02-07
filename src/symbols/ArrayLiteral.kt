package symbols

class ArrayLiteral(
        private val values: kotlin.Array<Expr>,
        private val t: Type
) : Expr(Array(t, values.size)) {

    init {
        for (value in values) {
            if (value.type != t) {
                valid = false;
                System.err.println("Expecting type : " + type + " but actual type: " + value.type)
            }
        }
    }
}
