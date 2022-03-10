package parse.expr

import ErrorHandler
import ErrorType
import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.*
import parse.symbols.Array
import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int

class UnaryOp(
    val e: Expr,
    t: Type,
    val op: UnaryOperator
) : Expr(t, 1 + e.weight) {

    init {
        when (op) {
            UnaryOperator.CHR,
            UnaryOperator.NEG,
            UnaryOperator.BITWISE_NOT -> {
                if (e.type != Int) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: INT, actual ${e.type})"
                    )
                }
            }
            UnaryOperator.LEN -> {
                if (e.type !is Array) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: ARRAY, actual ${e.type}"
                    )
                }
            }
            UnaryOperator.ORD -> {
                if (e.type != Char) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: CHAR, actual ${e.type})"
                    )
                }
            }
            UnaryOperator.NOT -> {
                if (e.type != Boolean) {
                    ErrorHandler.printErr(
                        ErrorType.SEMANTIC,
                        "Incompatible type at $e (expected: BOOLEAN, actual ${e.type})"
                    )
                }
            }
        }
    }

    override fun accept(v: ASTVisitor): List<Instruction> {
        return v.visitUnaryOpNode(this)
    }

    override fun toString(): String {
        return "$op $e"
    }
}

