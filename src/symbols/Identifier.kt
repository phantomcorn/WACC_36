package symbols

import kotlin.Boolean

abstract class Identifier{
    @JvmName("getValid1")
    fun getValid(): Boolean {
        return valid
    }

    protected var valid = true
}
