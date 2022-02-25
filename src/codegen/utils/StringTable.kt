package codegen.utils

import codegen.instr.loadable.Msg

class StringTable() {
    val dict: MutableMap<String, Msg> = mutableMapOf<String, Msg>()

    fun add(word: String): Msg {
        if (!(word in dict)) {
            dict[word] = Msg("msg_$n")
            n += 1
        }
        return dict[word]!!
    }

    companion object {
        var n = 0
    }
}
