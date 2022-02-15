package expr

import symbols.Type

abstract class UnaryOp(e: Expr, t: Type) : Expr(t)
