package symbols

abstract class Expr(val type: Type) : Identifier() {
    abstract fun check(): kotlin.Boolean
}
