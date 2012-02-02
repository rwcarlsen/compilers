

/** Symbol class used in compiler symbol table. */
public class Sym {

  private String type;

  /** Initialize Sym with specified type. */
  public Sym(String type) {
    this.type = type;
  }

  /** Get this symbol's type. */
  public String getType() {
    return this.type;
  }

  /** Get nicely formatted string representing the symbol. */
  public String toString() {
    return this.type;
  }
}

