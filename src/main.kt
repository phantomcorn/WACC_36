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
import parse.semantics.LexerListener
import org.antlr.v4.runtime.misc.ParseCancellationException
import parse.func.FuncType

fun main(args: Array<String>) {

    println("-- Compiling...")
    
    val input = CharStreams.fromFileName(args[0])

    /* Perform lexical analysis. */

    val lexer = WACCLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(LexerListener)

    val tokens = CommonTokenStream(lexer)

    /* Perform syntax analysis. */

    val parser = WACCParser(tokens)

    var parseTree: WACCParser.ProgContext? = null
    try {
        parseTree = parser.prog()
    } catch (e: ParseCancellationException) {
        ErrorHandler.printErr(ErrorType.SYNTAX, "")
    }
    //System.err.println(parseTree.toStringTree(parser))

    if (parser.numberOfSyntaxErrors > 0) {
        ErrorHandler.printErr(
            ErrorType.SYNTAX,
            ""
        )
    }

    /* Perform semantic analysis. */

    val visitor = Visitor()
    val ast = visitor.visit(parseTree!!)

    if (ErrorHandler.errorCount > 0) {
        System.err.println("Errors detected during compilation! Exit code " + ErrorType.SEMANTIC.code() + " returned.")
        exitProcess(ErrorType.SEMANTIC.code())
    }

    if (ast !is ASTNode) {
        System.err.println("Parser does not return an ASTNode")
        exitProcess(ErrorType.SEMANTIC.code())
    }
    for (funcName in visitor.functionST.dict.keys) {
        if (visitor.functionST.lookup(funcName) is FuncAST) {
            WaccTreeVisitor.funcTable.add(funcName, FuncObj(funcName))
        }
    }

    for (funcName in visitor.functionST.dict.keys) {
        val f = visitor.functionST.lookup(funcName)
        if (f is FuncType) {
            continue
        }
        val funcVisitor = WaccTreeVisitor((f as FuncAST).st)
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
    if (stringTable.dict.isNotEmpty()) {
        body.append(".data\n\n")
    }
    for (str in stringTable.dict.keys) {
        body.append("${stringTable.get(str).s}:\n")
        var i = 0
        var length = 0
        while (i < str.length) {
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
    body.append("\tPUSH {lr}\n${ARMInstructionVisitor().visitInstructions(allocatedCodeGen)}\tLDR r0, =0\n\tPOP {pc}\n\t.ltorg\n")

    for (func in funcTable.dict.keys) {
        val f = funcTable.lookup(func) as FuncObj
        body.append("${f.funcName}:\n")
	//don't push/pop when its a throw function
	    if (f.funcName.startsWith("p_throw")) {
	        body.append(ARMInstructionVisitor().visitInstructions(f.funcBody))
        } else if (f.user) {
	        body.append("\tPUSH {lr}\n" + ARMInstructionVisitor().visitInstructions(f.funcBody) + "\tPOP {pc}\n.ltorg\n")
	    } else {
            body.append("\tPUSH {lr}\n${ARMInstructionVisitor().visitInstructions(f.funcBody)}\tPOP {pc}\n")
	    }
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
