package codegen.utils

import parse.expr.ArrayElem
import parse.expr.BinaryOp
import parse.expr.BinaryOperator
import parse.expr.BooleanLiteral
import parse.expr.CharLiteral
import parse.expr.IntLiteral
import parse.expr.Literal
import parse.expr.Variable
import parse.expr.UnaryOp
import parse.expr.UnaryOperator
import parse.expr.Expr
import parse.symbols.Boolean
import parse.symbols.Int

object ExprEvaluate {
    fun evaluate(e: Expr): Expr {
        return when (e) {
            is BinaryOp -> evaluate(e)
            is UnaryOp -> evaluate(e)
            else -> e
        }    
    }

    fun evaluate(e: BinaryOp): Expr {
        val e1 = evaluate(e.e1) 
        val e2 = evaluate(e.e2) 

        when (e.binOp) {
            BinaryOperator.AND -> {
                if (e1 is BooleanLiteral) {
                    if (e1.value!!) {
                        return e2
                    } else {
                        return e1
                    }
                } 
                else if (e2 is BooleanLiteral) {
                    if (e2.value!!) {
                        return e1
                    } else {
                        return e2
                    }
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.AND)
                }
            }
            BinaryOperator.OR -> {
                if (e1 is BooleanLiteral) {
                    if (e1.value!!) {
                        return e1
                    } else {
                        return e2
                    }
                } 
                else if (e2 is BooleanLiteral) {
                    if (e2.value!!) {
                        return e2
                    } else {
                        return e1
                    }
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.OR)
                }
            }
            else -> {
                return e
            }
        }
    }

    fun evaluate(e: UnaryOp): Expr {
        val e1 = evaluate(e.e)
        return when (e.op) {
            UnaryOperator.CHR -> {
                if (e1 is IntLiteral) {
                    CharLiteral(e1.value!!.toChar().toString())
                } else {
                    UnaryOp(e1, e.type!!, e.op)
                }
            }
            UnaryOperator.ORD -> {
                if (e1 is CharLiteral) {
                    IntLiteral(e1.value!!.code)
                } else {
                    UnaryOp(e1, e.type!!, e.op)
                }
            }
            UnaryOperator.NEG -> {
                if (e1 is IntLiteral) {
                    IntLiteral((-e1.value!!).toString())
                } else {
                    UnaryOp(e1, e.type!!, e.op)
                }
            }
            UnaryOperator.NOT -> {
                if (e1 is BooleanLiteral) {
                    BooleanLiteral((!e1.value!!).toString())
                } else {
                    UnaryOp(e1, e.type!!, e.op)
                }
            }
            else -> UnaryOp(e1, e.type!!, e.op)
        }
    }
}
