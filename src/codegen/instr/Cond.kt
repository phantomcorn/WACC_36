package codegen.instr

enum class Cond {
    EQ {
        override fun toString(): String {
            return "EQ"
        }
    },
    NE {
        override fun toString(): String {
            return "NE"
        }
    },
    HSCS {
        override fun toString(): String {
            return "HSCS"
        }
    },
    LOCC {
        override fun toString(): String {
            return "LOCC"
        }
    },
    MI {
        override fun toString(): String {
            return "MI"
        }
    },
    PL {
        override fun toString(): String {
            return "PL"
        }
    },
    VS {
        override fun toString(): String {
            return "VS"
        }
    },
    VC {
        override fun toString(): String {
            return "VC"
        }
    },
    HI {
        override fun toString(): String {
            return "HI"
        }
    },
    LS {
        override fun toString(): String {
            return "LS"
        }
    },
    GE {
        override fun toString(): String {
            return "GE"
        }
    },
    LT {
        override fun toString(): String {
            return "LT"
        }
    },
    GT {
        override fun toString(): String {
            return "GT"
        }
    },
    LE {
        override fun toString(): String {
            return "LE"
        }
    },
    AL {
        override fun toString(): String {
            return ""
        }
    }
}