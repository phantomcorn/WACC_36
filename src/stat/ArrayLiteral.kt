package stat

import codegen.ASTVisitor
import expr.Expr
import expr.Literal
import codegen.instr.Instruction
import symbols.ArrayInstance
import symbols.Type

class ArrayLiteral(
    values: kotlin.Array<Expr>,
    t: Type?
) : Literal<kotlin.Array<Expr>>(ArrayInstance(t)), AssignRhs {

    init {
        value = values
        for (value in values) {
            if (value.type != t) {
                ErrorHandler.printErr(
                    ErrorType.SEMANTIC,
                    "Incompatible type at $value (expected: $t, actual: ${value.type})"
                )
            }
        }
    }

    override fun toString(): String {
        var result: String = "["
        if (value == null) {
            return "Null"
        } else {
            if (value!!.size > 0) {
                result = result + value!![0]
                for (i in 1..(value!!.size - 1)) {
                    result = ", " + value!![i]
                }
            }
        }
        return result + "]"
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        TODO("Not yet implemented")
    }
}
