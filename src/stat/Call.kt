package stat

import expr.Expr
import func.Function
import func.FuncType
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class Call(val values: kotlin.Array<Expr>, val id: kotlin.String, st: SymbolTable) : Identifier(), AssignRhs {
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
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incorrect number of parameters for $id (expected: " +
                    func.params.values.size + ", actual: " + values.size + ")"
                )
                Identifier.valid = false
            } else {
                type = func.returnType
                for (i in 0..(func.params.values.size - 1)) {
                    if (values[i].type != func.params.values[i].paramType) {
                        Identifier.valid = false
                        ErrorHandler.printErr(
                            ErrorType.SEMANTIC,
                            "Incompatible type at " + values[i] +
                            " (expected: " + func.params.values[i].paramType +
                            ", actual: " + values[i].type + ")"
                        )
                    }
                }
            }
        } else {
            val ft = func as FuncType
            if (ft.params.size != values.size) {
                Identifier.valid = false
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incorrect number of parameters for $id (expected: " +
                    ft.params.size + ", actual: " + values.size + ")"
                )

            } else {
                type = ft.returnType
                for (i in 0..(ft.params.size - 1)) {
                    if (values[i].type != ft.params[i]) {
                        Identifier.valid = false
                        ErrorHandler.printErr(
                            ErrorType.SEMANTIC,
                            "Incompatible type at " + values[i] + " (expected: " +
                            ft.params[i] + ", actual: " + values[i].type + ")"
                        )
                    }
                }
            }
        }
    }

    override fun type(): Type? = type

    override fun toString(): kotlin.String {
        var result = "call $id("
        if (values.size > 0) {
            result = result + values[0]
            for (i in 1..(values.size - 1)) {
                result = result + ", " + values[i]
            }
        }
        return result + ")"
    }
}
