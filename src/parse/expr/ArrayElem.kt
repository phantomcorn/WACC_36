package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import codegen.instr.loadable.Loadable
import parse.stat.AssignLhs
import parse.symbols.Array
import parse.symbols.Int
import parse.symbols.Type

class ArrayElem(
    val id: kotlin.String,
    val values: kotlin.Array<Expr>,
    val dims: kotlin.Int,
    t: Type?
) : Expr(t?.getBaseType(), 1), AssignLhs {
    init {
        if (t == null) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Array $id not defined"
            )
        } else if (t !is Array) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type (expected: Array, actual $t)"
            )
        } else if (t.getDim() != dims) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Dimension Error (expected: ${t.getDim()}, actual: $dims)"
            )
        } else {
            for (e in values) {
                if (e.type != Int) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible array element type (expected: Int, actual ${e.type}"
                    )
                }
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitArrayElemNode(this)
    }

    override fun acceptLhs(v: ASTVisitor): Pair<List<Instruction>, Loadable> {
        return v.visitArrayElemLhs(this)
    }
}
