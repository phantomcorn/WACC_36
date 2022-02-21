package parse.stat

import codegen.ASTVisitor
import codegen.instr.Instruction

class StatList(stat1 : Stat, stat2 : Stat) : Stat() {

    val list = mutableListOf<Stat>()

    init {
        if (stat1 is StatList) {
            list.addAll(stat1.list)
        } else {
            list.add(stat1)
        }

        if (stat2 is StatList) {
            list.addAll(stat2.list)
        } else {
            list.add(stat2)
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitStatListNode(this)
    }
}