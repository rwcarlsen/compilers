
import java.util.*;

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
  protected String type;
  protected String name;

  public Sym(String type, String name) {
    this.type = type;
    this.name = name;
  }

  public String getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public String toString() {
    return this.name + "(" + this.type + ")";
  }

  public boolean isFunc() {
    return false;
  }
}

class FnSym extends Sym {
  private String type;
  private String name;
  private LinkedList<Sym> args;

  public FnSym(String type, String name) {
    super(type, name);
    this.type = type;
    this.name = name;
    this.args = new LinkedList<Sym>();
  }

  public String getType() {
    return this.type;
  }

  public void addArg(Sym arg) {
    this.args.add(arg);
  }

  public String getName() {
    return this.name;
  }

  public String toString() {
    String str = this.name + "(";
    for (Sym arg : this.args) {
      str += arg.type;
      if (arg != this.args.getLast()) {
        str += ", ";
      }
    }
    str += " -> " + this.type + ")";
    return str;
  }

  public boolean isFunc() {
    return true;
  }
}
