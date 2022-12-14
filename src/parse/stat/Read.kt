package parse.stat

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Char
import parse.symbols.Int

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
        return v.visitReadNode(this)
    }

    override fun toString(): String = "read $lhs"
}
