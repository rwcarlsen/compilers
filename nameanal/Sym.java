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
  private String type;
  private String name;

  // constructor
  public Sym(String type, String name) {
    this.type = type;
    this.name = name;
  }

  // accessor

  public String getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  // other methods

  public String toString() {
    return "type=" + this.type + ", name=" + this.name;
  }
}
