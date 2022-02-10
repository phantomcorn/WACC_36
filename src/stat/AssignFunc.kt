package stat

import expr.Expr
import func.Function
import symbols.Identifier
import symbols.Type
import visitor.SymbolTable

class AssignFunc(id: String, args : Array<Expr>, st: SymbolTable) : Stat(), AssignRhs {
    var type : Type? = null
    init {
        val func = st.lookupAll(id)
        if (func == null) {
            Identifier.valid = false
        } else {
            val funcCast = func as Function
            type = funcCast.returnType
            var i = 0
            for (expr in args){
                if(expr.type != funcCast.params.values[i].paramType){
                    Identifier.valid = false
                    break
                }
                i++
            }
        }
    }

    override fun type() : Type?{
        return type
    }
}
