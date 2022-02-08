package expr

import symbols.Identifier
import symbols.Type

abstract class Expr(val type: Type?) : Identifier(), AssignRhs
