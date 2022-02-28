package stat

class Semi(val s1: Stat, val s2: Stat) : Stat() {
    override fun toString(): String = "$s1 ; $s2"
}
