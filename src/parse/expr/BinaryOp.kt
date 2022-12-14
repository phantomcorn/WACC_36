package parse.expr


import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int
import parse.symbols.Type
import kotlin.math.max
import kotlin.math.min


class BinaryOp(
    val e1: Expr,
    val e2: Expr,
    t: Type,
    val binOp: BinaryOperator
) : Expr(t, min(max(1 + e1.weight, e2.weight), max(e1.weight, 1 + e2.weight))) {
    init {
        when (binOp) {
            BinaryOperator.AND, BinaryOperator.OR -> {
                if (e1.type != Boolean) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: BOOL, actual: ${e1.type})"
                    )
                } else if (e2.type != Boolean) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: BOOL, actual: ${e2.type})"
                    )
                }
            }

            BinaryOperator.PLUS, BinaryOperator.MINUS, BinaryOperator.DIV, BinaryOperator.MULTI, BinaryOperator.MOD,
            BinaryOperator.BITWISE_AND, BinaryOperator.BITWISE_OR, BinaryOperator.BITWISE_XOR,
            BinaryOperator.LOGICAL_SHIFT_LEFT, BinaryOperator.LOGICAL_SHIFT_RIGHT -> {
                if (e1.type != Int) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: INT, actual: ${e1.type})"
                    )
                } else if (e2.type != Int) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: INT, actual: ${e1.type})"
                    )
                }
            }

            BinaryOperator.GT, BinaryOperator.GTE, BinaryOperator.LT, BinaryOperator.LTE -> {
                if (e1.type != e2.type) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Mismatched expression types at $this with + ${e1.type} and ${e2.type})"
                    )
                } else if ((e1.type != Int) && e1.type != Char) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: {INT, CHAR}, actual: ${e1.type})"
                    )
                }
            }
            BinaryOperator.EQUIV, BinaryOperator.NOTEQUIV -> {
                if (e1.type != e2.type) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Mismatched expression types at $this with + ${e1.type} and ${e2.type})"
                    )
                }
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitBinaryOp(this)
    }

    override fun toString(): String {
        return "$e1 $binOp $e2"
    }
}
