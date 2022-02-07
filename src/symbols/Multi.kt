package symbols

import kotlin.Boolean
import kotlin.Int

class Multi(
    val e1: Expr,
    val e2: Expr
) : BinaryOp(e1, e2, Boolean) {

    override fun check(): kotlin.Boolean {
        return e1.type == Int && e2.type == Int
    }
}
