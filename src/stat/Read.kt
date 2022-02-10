package stat

import symbols.Char
import symbols.Identifier
import symbols.Int

class Read(val lhs: AssignLhs) : Stat() {
    init {
        if (!(lhs.type() == Int || lhs.type() == Char)) {
            ErrorHandler.printErr(
                ErrorType.SEMANTIC,
                "Incompatible type at $this (expected: {INT, CHAR}, actual: ${lhs.type()}"
            )
            Identifier.valid = false
        }
    }
}
