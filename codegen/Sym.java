import java.util.*;

// **********************************************************************
// The Sym class defines a symbol-table entry;
// an object that contains a type (a String).
// **********************************************************************
//
// Methods
// =======
//
// constructor
// -----------
// Sym(String S)         -- constructor: creates a Sym with the given type
//
// accessor
// --------
// type()             -- returns this symbol's type
//
// other
// -----
// makeComplete()    -- marks this sym as complete
// toString()        -- prints the values associated with this Sym

class Sym {
  public Sym(String T) {
    myType = T;
    myComplete = true;
  }

  public Sym(String T, boolean comp) {
    this(T);
    myComplete = comp;
  }

  public String type() {
    return myType;
  }

  public String toString() {
    return "";
  }

  public boolean isFunc() {
    return false;
  }

  // fields
  protected String myType;
  private boolean myComplete;
  public int offset;
}

// **********************************************************************
// The FnSym class is a subclass of the Sym class, just for functions.
// The myReturnType field holds the return type, and there are new fields
// to hold information about the parameters.
// **********************************************************************
class FnSym extends Sym {
  public FnSym(String T, int numparams) {
    super("->"+T);
    myReturnType = T;
    myNumParams = numparams;
    myParamTypes = null;
  }

  public void addFormals(LinkedList<Sym> L) {
    myParamTypes = new LinkedList<String>();
    // UPDATE TYPE STRING
    boolean first = true;
    Iterator<Sym> it = L.descendingIterator();
    while (it.hasNext()) {
      Sym oneSym = it.next();
      myParamTypes.add(0, oneSym.type());
      if (first) {
        myType = oneSym.type() + myType;
        first = false;
      } else {
        myType = oneSym.type() + "," + myType;
      }
      if (oneSym.type().equals("int")) {
        this.paramSize += 4;
        this.locSize -= 4;
      } else if (oneSym.type().equals("double")) {
        this.paramSize += 8;
        this.locSize -= 8;
      }
    }
  }

  public String returnType() {
    return myReturnType;
  }

  public int numparams() {
    return myNumParams;
  }

  public LinkedList<String> paramTypes() {
    return myParamTypes;
  }

  public boolean isFunc() {
    return true;
  }

  public void addLocSize(int size) {
    this.locSize += size;
  }

  public int locSize() {
    return this.locSize;
  }

  public int paramSize() {
    return this.paramSize;
  }

  // new fields
  private String myReturnType;
  private int myNumParams;
  private LinkedList<String> myParamTypes;
  private int locSize;
  private int paramSize;
}


