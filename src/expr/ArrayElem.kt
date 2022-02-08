package expr

import stat.AssignLhs

class ArrayElem(e: Expr) : Expr(e.type), AssignLhs
