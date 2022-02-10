package stat

class Begin(val s: Stat) : Stat() {
    override fun toString(): String = "begin $s end"
}
