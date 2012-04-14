// **********************************************************************
// The Sym class defines a symbol-table entry;
// an object that contains a "type" (a string)
// **********************************************************************
//
// Methods
// =======
//
// constructor
// -----------
// Sym(String type)     -- constructor: creates a Sym with the given type
//
// accessor
// --------
// getType()             -- returns this symbol's type
//
// other
// -----
// toString()        -- prints the values associated with this Sym

class Sym {
    // private field
    private String myType;

    // constructor
    public Sym(String type) {
	myType = type;
    }

    // accessor

    // **********************************************************************
    // getType
    // **********************************************************************
    public String getType() {
	return myType;
    }

    // other methods

    // **********************************************************************
    // toString
    // **********************************************************************
    public String toString() {
	return myType;
    }
}
