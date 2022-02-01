class SymbolTable(private val parent: SymbolTable?) {

    val dict : MutableMap<String,Identifier> = mutableMapOf<String,Identifier>()

    fun getTable():SymbolTable? {
        return parent;
    }

    fun add(name : String, elem : Identifier) {
        dict.put(name, elem);
    }

    fun lookup(name: String): Identifier? {
        return dict[name];
    }

    fun lookupAll(name: String): Identifier? {
        var currTable : SymbolTable? = this;
        while (currTable != null){
            var res : Identifier? = currTable.lookup(name);
            if (res !== null) {
                return res;
            }
            currTable = currTable.parent;
        }
        return null;
    }


}