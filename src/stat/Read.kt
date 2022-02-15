package stat

import codegen.ASTVisitor
import instr.Instruction
import symbols.Char
import symbols.Int

class Read(val lhs: AssignLhs) : Stat() {
    init {
        if (!(lhs.type() == Int || lhs.type() == Char)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $this (expected: {Int, Char}, actual: ${lhs.type()}"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString(): String = "read $lhs"
}
