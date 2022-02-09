package stat

import expr.Expr
import func.Function
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Call(val values: kotlin.Array<Expr>, id: kotlin.String, st: SymbolTable) : Identifier(), AssignRhs {
    var type: Type? = null

    init {
        val func = st.lookupAll(id)
        if (func == null) {
            System.err.println("function " + id + " has not been declared")
            valid = false
        } else if (!(func is Function)) {
            System.err.println(id + " is not a function")
            valid = false
        } else if (func.params.values.size != values.size) {
            System.err.println("Expected " + func.params.values.size + " args, got " + values.size)
            valid = false
        } else {
            type = func.returnType
            for (i in 0..(func.params.values.size - 1)) {
                if (values[i].type != func.params.values[i].paramType) {
                    valid = false
                    System.err.print("Expecting type : " + func.params.values[i].paramType)
                    System.err.println(" but actual type: " + values[i].type)
                }
            }
        }
    }

    override fun type(): Type? = type
}
