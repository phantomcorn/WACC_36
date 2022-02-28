package stat

import expr.Expr

class Println(val e: Expr) : Stat() {
    override fun toString(): String = "println $e"
}
