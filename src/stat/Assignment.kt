package stat

import codegen.ASTVisitor
import instr.Instruction

class Assignment(val lhs: AssignLhs, val rhs: AssignRhs) : Stat() {
    init {
        if (lhs.type() != rhs.type()) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $rhs (expected: ${lhs.type()}, actual: ${rhs.type()})"
            )
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun toString() = "$lhs = $rhs"
}
