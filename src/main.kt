import antlr.WACCLexer
import antlr.WACCParser
import codegen.ARMInstructionVisitor
import codegen.ASTNode
import codegen.AllocRegPass
import codegen.WaccTreeVisitor
import codegen.instr.FuncObj
import codegen.utils.RegisterIterator
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import parse.func.FuncAST
import parse.semantics.Visitor
import java.io.FileWriter
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    println("-- Compiling...")
    
    val input = CharStreams.fromFileName(args[0])

    /* Perform lexical analysis. */

    val lexer = WACCLexer(input)

    val tokens = CommonTokenStream(lexer)

    /* Perform syntax analysis. */

    val parser = WACCParser(tokens)

    val parseTree = parser.prog()
    //System.err.println(parseTree.toStringTree(parser))

    if (parser.numberOfSyntaxErrors > 0) {
        ErrorHandler.printErr(
            ErrorType.SYNTAX,
            ""
        )
    }

    /* Perform semantic analysis. */

    val visitor = Visitor()
    val ast = visitor.visit(parseTree)

    if (ErrorHandler.errorCount > 0) {
        System.err.println("Errors detected during compilation! Exit code " + ErrorType.SEMANTIC.code() + " returned.")
        exitProcess(ErrorType.SEMANTIC.code())
    }

    if (ast !is ASTNode) {
        System.err.println("Parser does not return an ASTNode")
        exitProcess(ErrorType.SEMANTIC.code())
    }
    for (funcName in visitor.functionST.dict.keys) {
        WaccTreeVisitor.funcTable.add(funcName, FuncObj(funcName))
    }

    for (funcName in visitor.functionST.dict.keys) {
        val f = visitor.functionST.lookup(funcName) as FuncAST
        val funcVisitor = WaccTreeVisitor(f.st)
        funcVisitor.visitFunction(f)
    }

    val stringTable = WaccTreeVisitor.stringTable
    val funcTable = WaccTreeVisitor.funcTable

    val treeVisitor = WaccTreeVisitor(visitor.currentSymbolTable)
    val intermediateCodeGen = treeVisitor.visitAST(ast)


    val regAllocator = AllocRegPass(RegisterIterator.max)

    for (func in funcTable.dict.keys) {
        val f = funcTable.lookup(func) as FuncObj
        val instrs = regAllocator.visitInstructions(f.funcBody).toList()
        f.funcBody.clear()
        f.funcBody.addAll(instrs)
    }

    val allocatedCodeGen = regAllocator.visitInstructions(intermediateCodeGen)

    val body = StringBuilder()
    if (stringTable.dict.size > 0) {
        body.append(".data\n\n")
    }
    for (str in stringTable.dict.keys) {
        body.append("${stringTable.get(str).s}:\n")
        var i = 0
        var length = 0
        while (i < str.length - 1) {
            length += 1
            if (str[i] == '\\') {
                i += 1
            }
            i += 1
        }
        body.append("\t.word $length\n")
        body.append("\t.ascii \"${str}\"\n\n")
    }

    body.append(".text\n\n")
    body.append(".global main\n")
    body.append("main:\n")
    body.append("${ARMInstructionVisitor().visitInstructions(allocatedCodeGen)}\n")

    for (func in funcTable.dict.keys) {
        val f = funcTable.lookup(func) as FuncObj
        body.append("${f.funcName}:\n")
        body.append(ARMInstructionVisitor().visitInstructions(f.funcBody))
        body.append("\n")
    }

    val fileName = args[0].substringAfterLast("/").substringBeforeLast(".") + ".s" 
    try {
        val f = FileWriter(fileName, false)
        f.write(body.toString())
        f.flush()
        f.close()
    } catch (e: Exception) {
        System.err.println("Error writing to output file!")
        exitProcess(1)
    }

    println("-- Printing Assembly...")

    println("$fileName contents are:")

    println("-----------------------------------------------------------")

    /* paste assembly here */

    println(body)

    println("-----------------------------------------------------------")

    println("-- Finished")
}
