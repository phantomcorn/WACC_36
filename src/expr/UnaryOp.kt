package expr

import symbols.Type

abstract class UnaryOp(val e: Expr, t: Type) : Expr(t)
