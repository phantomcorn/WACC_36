package parse.expr

enum class BinaryOperator {
    AND {
        override fun toString(): String = "&&"
    },
    OR {
        override fun toString(): String = "||"
    },
    MULTI {
        override fun toString(): String = "*"
    },
    DIV {
        override fun toString(): String = "/"
    },
    MOD {
        override fun toString(): String = "%"
    },
    PLUS {
        override fun toString(): String = "+"
    },
    MINUS {
        override fun toString(): String = "-"
    },
    GT {
        override fun toString(): String = ">"
    },
    GTE {
        override fun toString(): String = ">="
    },
    LT {
        override fun toString(): String = "<"
    },
    LTE {
        override fun toString(): String = "<="
    },
    EQUIV {
        override fun toString(): String = "=="
    },
    NOTEQUIV {
        override fun toString(): String = "!="
    },
    BITWISE_AND {
        override fun toString(): String = "&"
    },
    BITWISE_OR {
        override fun toString(): String = "|"
    },
    BITWISE_XOR {
        override fun toString() : String = "^"
    };

    abstract override fun toString(): String
}