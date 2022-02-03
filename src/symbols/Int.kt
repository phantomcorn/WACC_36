package symbols

import kotlin.Int

class Int : Type() {

    companion object {
        const val min : Int = Int.MIN_VALUE;
        const val max : Int = Int.MAX_VALUE;
    }

}