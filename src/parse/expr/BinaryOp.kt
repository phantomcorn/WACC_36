package parse.expr


import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int
import parse.symbols.Type



class BinaryOp(val e1: Expr, val e2: Expr, t: Type, val binOp : BinaryOperator) : Expr(t) {

    init {
        when (binOp) {

            BinaryOperator.AND, BinaryOperator.OR -> {
                if (e1.type != Boolean) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: BOOL, actual: ${e1.type})"
                    )
                } else if (e2.type != Boolean) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: BOOL, actual: ${e2.type})"
                    )
                }
            }

            BinaryOperator.PLUS, BinaryOperator.MINUS, BinaryOperator.DIV, BinaryOperator.MULTI, BinaryOperator.MOD -> {
                if (e1.type != Int) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this.toString() (expected: INT, actual: ${e1.type})"
                    )
                } else if (e2.type != Int) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: INT, actual: ${e1.type})"
                    )
                }
            }

            BinaryOperator.GT, BinaryOperator.GTE, BinaryOperator.LT, BinaryOperator.LTE  -> {
                if (e1.type != e2.type) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Mismatched expression types at $this with + ${e1.type} and ${e2.type})"
                    )
                } else if ((e1.type != Int) && e1.type != Char) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: {INT, CHAR}, actual: $e1.type)"
                    )
                }
            }
            BinaryOperator.EQUIV, BinaryOperator.NOTEQUIV -> {
                if (e1.type != e2.type) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
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
        return when (binOp) {
            BinaryOperator.AND -> "$e1 && $e2"
            BinaryOperator.OR -> "$e1 || $e2"
            BinaryOperator.MULTI -> "$e1 * $e2"
            BinaryOperator.DIV -> "$e1 / $e2"
            BinaryOperator.MOD -> "$e1 % $e2"
            BinaryOperator.PLUS -> "$e1 + $e2"
            BinaryOperator.MINUS -> "$e1 - $e2"
            BinaryOperator.GT -> "$e1 > $e2"
            BinaryOperator.GTE -> "$e1 >= $e2"
            BinaryOperator.LT -> "$e1 < $e2"
            BinaryOperator.LTE -> "$e1 <= $e2"
            BinaryOperator.EQUIV -> "$e1 == $e2"
            BinaryOperator.NOTEQUIV -> "$e1 != $e2"
        }

    }
}
