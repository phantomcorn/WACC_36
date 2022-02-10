package stat

import expr.Expr
import symbols.Boolean
import symbols.Identifier

class While(val e: Expr, val s: Stat) : Stat() {
    init {
        if (e.type !is Boolean) {
            System.err.println("While condition \"${e.toString()}\" does not evaluate to a Boolean. Got : ${e.type}")
            Identifier.valid = false
        }
    }
}
