package symbols

class ArrayLiteral(
        private val values: kotlin.Array<Expr>,
        private val t: Type
) : Expr(Array(t, values.size)) {


    override fun check(): kotlin.Boolean {
        for (value in values) {
            if (!Utilities.typeCompat(value.type, t)) {
                println("Expecting type : " + type + " but actual " + value.type)
            }
        }
        return true;
    }
}
