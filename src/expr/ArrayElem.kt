package expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import stat.AssignLhs
import symbols.Array
import symbols.Int
import symbols.Type

class ArrayElem(
    val id: kotlin.String,
    val values: kotlin.Array<Expr>,
    val dims: kotlin.Int,
    t: Type?
) : Expr(t?.getBaseType()), AssignLhs {
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
}
