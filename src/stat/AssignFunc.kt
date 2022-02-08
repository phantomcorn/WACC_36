package stat

import expr.Expr
import myvisitor.SymbolTable
import symbols.Type

class AssignFunc(id: String, args : Array<Expr>, st: SymbolTable) : Stat(), AssignRhs {
    var type : Type? = null
    init {
        val func = st.lookupAll(id)
        if (func == null) {
            valid = false
        } else {
            val funcCast = func as symbols.Function
            type = funcCast.returnType
            var i = 0
            for (expr in args){
                if(expr.type != funcCast.params[i].paramType){
                    valid = false
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
