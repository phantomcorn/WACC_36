package codegen.utils

import parse.symbols.Identifier
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
    fun evaluate(e: Identifier): Identifier {
        return when (e) {
            is BinaryOp -> evaluate(e)
            is UnaryOp -> evaluate(e)
            else -> e
        }    
    }

    private fun evaluate(e: BinaryOp): Identifier {
        val e1 = e.e1
        val e2 = e.e2

        when (e.binOp) {
            BinaryOperator.AND -> {
                if (e1 is BooleanLiteral) {
                    if (e1.value != null && e1.value!!) {
                        return e2
                    } else {
                        return e1
                    }
                } 
                else if (e2 is BooleanLiteral) {
                    if (e2.value != null && e2.value!!) {
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
                    if (e1.value != null && e1.value!!) {
                        return e1
                    } else {
                        return e2
                    }
                } 
                else if (e2 is BooleanLiteral) {
                    if (e2.value != null && e2.value!!) {
                        return e2
                    } else {
                        return e1
                    }
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.OR)
                }
            }
            BinaryOperator.MULTI -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return IntLiteral(Integer.toString(e1.value!! * e2.value!!))
                } else {
                    return BinaryOp(e1, e2, Int, BinaryOperator.MULTI)
                }
            }
            BinaryOperator.DIV -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null && e2.value!! != 0) {
                    return IntLiteral(Integer.toString(e1.value!! / e2.value!!))
                } else {
                    return BinaryOp(e1, e2, Int, BinaryOperator.DIV)
                }
            }
            BinaryOperator.MOD -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return IntLiteral(Integer.toString(e1.value!! % e2.value!!))
                } else {
                    return BinaryOp(e1, e2, Int, BinaryOperator.MOD)
                }
            }
            BinaryOperator.PLUS -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return IntLiteral(Integer.toString(e1.value!! + e2.value!!))
                } else {
                    return BinaryOp(e1, e2, Int, BinaryOperator.PLUS)
                }
            }
            BinaryOperator.MINUS -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return IntLiteral(Integer.toString(e1.value!! - e2.value!!))
                } else {
                    return BinaryOp(e1, e2, Int, BinaryOperator.MINUS)
                }
            }
            BinaryOperator.GT -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! > e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.GT)
                }
            }
            BinaryOperator.GTE -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! >= e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.GTE)
                }
            }
            BinaryOperator.LT -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! < e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.LT)
                }
            }
            BinaryOperator.LTE -> {
                if (e1 is IntLiteral && e2 is IntLiteral && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! <= e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.LTE)
                }
            }
            BinaryOperator.EQUIV -> {
                if (e1 is Literal<*> && e2 is Literal<*> && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! == e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.EQUIV)
                }
            }
            BinaryOperator.NOTEQUIV -> {
                if (e1 is Literal<*> && e2 is Literal<*> && e1.value != null && e2.value != null) {
                    return BooleanLiteral((e1.value!! != e2.value!!).toString())
                } else {
                    return BinaryOp(e1, e2, Boolean, BinaryOperator.NOTEQUIV)
                }
            }
            else -> {
                return e
            } 
        }
    }

    private fun evaluate(e: UnaryOp): Identifier {
        val e1 = e.e
        return when (e.op) {
            UnaryOperator.CHR -> {
                if (e1 is IntLiteral && e1.value != null) {
                    CharLiteral(e1.value!!.toChar().toString())
                } else if (e.type != null) {
                    UnaryOp(e1, e.type, e.op)
                } else {
                    e
                }
            }
            UnaryOperator.ORD -> {
                if (e1 is CharLiteral) {
                    IntLiteral(e1.value!!.code.toString())
                } else if (e.type != null) {
                    UnaryOp(e1, e.type, e.op)
                } else {
                    e
                }
            }
            UnaryOperator.NEG -> {
                if (e1 is IntLiteral && e1.value != null) {
                    IntLiteral((-e1.value!!).toString())
                } else if (e.type != null) {
                    UnaryOp(e1, e.type, e.op)
                } else {
                    e
                }
            }
            UnaryOperator.NOT -> {
                if (e1 is BooleanLiteral && e1.value != null) {
                    BooleanLiteral((!e1.value!!).toString())
                } else if (e.type != null) {
                    UnaryOp(e1, e.type, e.op)
                } else {
                    e
                }
            }
            else -> {
                if (e.type != null) {
                    UnaryOp(e1, e.type, e.op)
                } else {
                    e
                }
            }
        }
    }
}
