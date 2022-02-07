package symbols

class Pair(firstElem : Expr, secondElem : Expr ,firstElemType: Type, secondElemType: Type) : Type() {

    init {
        if (firstElem.type != firstElemType) {
            valid = false
            System.err.println("Expecting type : " + firstElemType + " but actual type: " + firstElem.type)
        } else if (secondElem.type != secondElemType) {
            valid = false;
            System.err.println("Expecting type : " + secondElemType + " but actual type: " + secondElem.type)
        }
    }

}
