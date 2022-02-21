package parse.func

import parse.stat.*

object ReturnChecker {
    fun check(s: Stat): Boolean {
        var stat = s

        if (stat is StatList){
            stat = stat.list[stat.list.size - 1]
        }

        return when (stat) {
            is Return -> true
            is Exit -> true
            is If -> check(stat.s1) && check(stat.s2)
            is Begin -> check(stat.s)
            else -> false
        }
    }
}
