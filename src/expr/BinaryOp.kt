package expr

import symbols.Type

abstract class BinaryOp(val e1: Expr, val e2: Expr, t: Type) : Expr(t)