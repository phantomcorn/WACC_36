package stat

import expr.Expr

class Print(val e: Expr) : Stat() {
    override fun toString() = "print $e"
}
