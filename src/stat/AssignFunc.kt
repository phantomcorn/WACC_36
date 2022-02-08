package stat

import myvisitor.SymbolTable
import symbols.Type

class AssignFunc(id: String, st: SymbolTable) : Stat(), AssignRhs {
    var type : Type? = null
    init {
        val func = st.lookupAll(id)
        if (func == null) {
            valid = false
        } else {
            val funcCast = func as symbols.Function
            type = funcCast.returnType
        }
    }

    override fun type() : Type?{
        return type
    }
}
