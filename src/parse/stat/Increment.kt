package parse.stat

import codegen.ASTNode
import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.symbols.Int
import parse.symbols.Type

class Increment(val lhs: AssignLhs, val incrAmount: kotlin.Int) : Stat(), AssignLhs {

    init {
        if (!(lhs.type() is Int)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $lhs (expected: Int, actual: ${lhs.type()}"
            )
        }
    }


    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitIncrement(this)
    }

    override fun type(): Type {
        return Int
    }

    override fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable> {
        return v.visitIncrementLhs(this)
    }

}
