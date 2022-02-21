package parse.expr

import codegen.ASTVisitor
import codegen.instr.Instruction
import parse.symbols.*

class UnaryOp(val e: Expr, t: Type, private val op: UnaryOperator) : Expr(t) {

    init {
        when (op) {
            UnaryOperator.CHR,
            UnaryOperator.NEG -> {
                if (e.type != Int) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: INT, actual ${e.type})")
                }
            }
            UnaryOperator.LEN -> {
                if (e.type !is Array) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: ARRAY, actual ${e.type}")
                }
            }
            UnaryOperator.ORD -> {
                if (e.type != Char) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $this (expected: CHAR, actual ${e.type})")
                }
            }
            UnaryOperator.NOT -> {
                if (e.type != Boolean) {
                    ErrorHandler.printErr(ErrorType.SEMANTIC,
                        "Incompatible type at $e (expected: BOOLEAN, actual ${e.type})")
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

