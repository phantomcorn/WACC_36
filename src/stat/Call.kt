package stat

import expr.Expr
import func.Function
import func.FuncType
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Call(val values: kotlin.Array<Expr>, id: kotlin.String, st: SymbolTable) : Identifier(), AssignRhs {
    var type: Type? = null

    init {
        val func = st.lookupAll(id)
        if (func == null) {
            val types = mutableListOf<Type?>()
            for (e in values) {
                types.add(e.type)
            }
            st.add(id, FuncType(st, id, types.toTypedArray()))
        } else if (!(func is Function || func is FuncType)) {
            System.err.println(id + " is not a function")
            Identifier.valid = false
        } else if (func is Function) {
            if (func.params.values.size != values.size) {
                System.err.println("Expected " + func.params.values.size + " args, got " + values.size)
                Identifier.valid = false
            } else {
                type = func.returnType
                for (i in 0..(func.params.values.size - 1)) {
                    if (values[i].type != func.params.values[i].paramType) {
                        Identifier.valid = false
                        System.err.print("Expecting type : " + func.params.values[i].paramType)
                        System.err.println(" but actual type: " + values[i].type)
                    }
                }
            }
        } else {
            val func = func as FuncType
            if (func.params.size != values.size) {
                System.err.println("Expected " + func.params.size + " args, got " + values.size)
                Identifier.valid = false
            } else {
                type = func.returnType
                for (i in 0..(func.params.size - 1)) {
                    if (values[i].type != func.params[i]) {
                        Identifier.valid = false
                        System.err.print("Expecting type : " + func.params[i])
                        System.err.println(" but actual type: " + values[i].type)
                    }
                }
            }
        }
    }

    override fun type(): Type? = type
}
