package func

import stat.*

object ReturnChecker {
    fun check(s: Stat): kotlin.Boolean {
        return when (s) {
            is Return -> true
            is Exit -> true
            is If -> check(s.s1) && check(s.s2)
            is Begin -> check(s.s)
            is Semi -> check(s.s2)
            else -> false
        }
    }
}
