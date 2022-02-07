package symbols

class ArrayLiteral(
        private val values: kotlin.Array<Expr>,
        private val t: Type
) : Expr(Array(t, values.size)) {


    init {
        for (value in values) {
            if (value.type != t) {
                println("Expecting type : " + type + " but actual " + value.type)
            }
        }
    }
}
