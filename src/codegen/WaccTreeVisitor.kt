package codegen

import codegen.instr.*
import codegen.instr.operand2.Immediate
import codegen.instr.operand2.ImmediateChar
import codegen.instr.register.Register
import parse.expr.*
import parse.stat.*
import kotlin.collections.ArrayDeque
import codegen.utils.RegisterIterator

class WaccTreeVisitor() : ASTVisitor {
    val regsInUse = ArrayDeque<MutableSet<Register>>()
    val availableRegisters = RegisterIterator()

    /* Begin at root of AST. */

    override fun visitAST(root : ASTNode): List<Instruction> {
        return root.accept(this)
    }

    /* Code generation for statements. */

    override fun visitSkipNode(): List<Instruction> {
        return listOf<Instruction>()
    }

    override fun visitWhileNode(node: While): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitDeclarationNode(node: Declaration): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitAssignmentNode(node: Assignment): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitReadNode(node: Read): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitExitNode(node: Exit): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintNode(node: Print): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPrintlnNode(node: Println): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStatListNode(statList: StatList): List<Instruction> {
        return statList.list.flatMap { x ->  x.accept(this)}
    }

    override fun visitFreeNode(node: Free): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitIfNode(node: If): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBeginNode(node: Begin): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitReturnNode(node: Return): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitVariableNode(node: Variable): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitNewPairNode(node: NewPair): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCallNode(node: Call): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitEmptyArrayLiteralNode(node: EmptyArrayLiteral): List<Instruction> {
        TODO("Not yet implemented")
    }

    /*
    override fun visitArrayInstanceNode(node: ArrayInstance): List<Instruction> {
        TODO("Not yet implemented")
    }
    */

    /* Code generation for types. */

    override fun visitIntNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitTypelessPairNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairInstanceNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArgListNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairElemNode(): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayElemNode(node: ArrayElem): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for binary operators. */

    override fun visitBinaryOp(node : BinaryOp): List<Instruction> {

        val instructions = mutableListOf<Instruction>()

        val lhs : List<Instruction>
        val rhs : List<Instruction>

        val rd = availableRegisters.next()
        val rn = availableRegisters.next()


        if (node.e1.weight > node.e2.weight) {
            availableRegisters.add(rn)
            availableRegisters.add(rd)
            lhs = node.e1.accept(this) //regInUse stores rd
            rhs = node.e2.accept(this) //regInUse stores rn
        } else {
            availableRegisters.add(rd)
            availableRegisters.add(rn)
            rhs = node.e2.accept(this) //regInUse stores rn
            lhs = node.e1.accept(this) //regInUse stores rd
        }


        val binOpInstr : Instruction = when (node.binOp) {
            BinaryOperator.AND -> {
                And(rd, rd, rn)
            }
            BinaryOperator.OR -> {
                Or(rd, rd, rn)
            }
            BinaryOperator.MULTI -> {
                Multiply(rd, rd, rn)
            }

            BinaryOperator.DIV -> TODO()
            BinaryOperator.MOD -> TODO()
            BinaryOperator.PLUS -> {
                Add(rd, rd, rn)
            }
            BinaryOperator.MINUS -> {
                Subtract(rd, rd, rn)
            }
            BinaryOperator.GT, BinaryOperator.GTE, BinaryOperator.LT, BinaryOperator.LTE, BinaryOperator.EQUIV, BinaryOperator.NOTEQUIV -> {
                Compare(rd, rn)
            }

        }

        regsInUse.first().remove(rn) //remove rn

        instructions.addAll(lhs)
        instructions.addAll(rhs)
        instructions.add(binOpInstr)

        return instructions
    }

    /* Code generation for literals. */

    override fun visitIntLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitCharLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitBooleanLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitStringLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitPairLiteralNode(token: kotlin.String): List<Instruction> {
        TODO("Not yet implemented")
    }

    override fun visitArrayLiteralNode(node: ArrayLiteral): List<Instruction> {
        TODO("Not yet implemented")
    }

    /* Code generation for unary operators. */

    override fun visitUnaryOpNode(node: UnaryOp): List<Instruction> {

        val instructions : MutableList<Instruction> = emptyList<Instruction>() as MutableList<Instruction>

        //{0,1,2,3}
        val rd = availableRegisters.peek()
        val exprInstr : List<Instruction> = node.e.accept(this)

        val unOpInstr : Instruction = when (node.op) {
            UnaryOperator.CHR -> {
                Move(rd, ImmediateChar(rd.value.toChar()))
            }
            UnaryOperator.LEN -> TODO()
            UnaryOperator.ORD -> {
                Move(rd, ImmediateChar((node.e as CharLiteral).token as Char))
                //Could also do:
                //(node.e as CharLiteral).token as Char).value.first().code
            }
            UnaryOperator.NEG -> TODO()
            UnaryOperator.NOT -> TODO()
        }



        instructions.addAll(exprInstr)
        instructions.add(unOpInstr)

        return instructions
    }
}
