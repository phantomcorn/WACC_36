package parse.expr

import parse.symbols.Type

abstract class UnaryOp(val e: Expr, t: Type) : Expr(t)
