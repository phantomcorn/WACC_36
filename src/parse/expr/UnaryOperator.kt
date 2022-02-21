package parse.expr

import parse.symbols.Boolean
import parse.symbols.Char
import parse.symbols.Int
import parse.symbols.Type

enum class UnaryOperator(t: Type) {
    CHR(Int) { override fun toString(): String = "chr" },
    LEN(Int) { override fun toString(): String = "len" },
    ORD(Char) { override fun toString(): String = "ord" },
    NEG(Int) { override fun toString(): String = "neg" },
    NOT(Boolean) { override fun toString(): String = "not" };

    abstract override fun toString() : String
}