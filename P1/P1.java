
import java.util.*;
import java.io.PrintStream;
import java.io.OutputStream;


/**
 * Test fixture and tests
 */
public class P1 extends RobertTest {

  private SymTab tab;

  private String type1;
  private String name1;
  private Sym sym1;

  private String type2;
  private String name2;
  private Sym sym2;

  private String msgNull;

  /**
   * initialize the test fixture
   */
  public P1() {
    this.tab = new SymTab();

    this.name1 = "";
    this.type1 = "monkey";
    this.sym1 = new Sym(this.type1);

    this.type2 = "elephant";
    this.name2 = "foopid";
    this.sym2 = new Sym(this.type2);

    this.msgNull = "Expected null and received something else.";
  }

  /**
   * initiate and run all tests in this fixture/class
   */
  public static void main(String[] args) {

    RobertTest.go("P1");
  }

//---------------------------------------------------------------------------//
//------------------------- Sym tests ---------------------------------------//
//---------------------------------------------------------------------------//

  /**
   * Sym class def constructor sets type correctly.
   */
  public void testSym_constructor() {
    
    assertEQ(this.sym1.getType(), this.type1, "");
    assertEQ(this.sym2.getType(), this.type2, "");

    // repeat to be sure this method doesn't modify the object's type
    assertEQ(this.sym1.getType(), this.type1, "");
  }

  /**
   * Sym.toString method builds proper string representation
   */
  public void testSym_toString() {
    
    assertEQ(this.sym2.toString(), this.type2, "");

    // repeat to be sure this method doesn't modify the object's type
    assertEQ(this.sym2.toString(), this.type2, "");
  }

//---------------------------------------------------------------------------//
//-------------------------- SymTab tests -----------------------------------//
//---------------------------------------------------------------------------//

  /**
   * Map removal throws at appropriate times
   */
  public void testSymTab_removeMap() {
    String msg;

    try {
      this.tab.removeMap();
    } catch (EmptySymTabException err) {
      msg = "Initialized SymTab does not contain an "; 
      msg += "empty hashmap in symbol table list.";
      fail(msg);
    }

    try {
      this.tab.removeMap();
      msg = "Initialized SymTab contains too many";
      msg += " hashmaps in the symbol table."; 
      msg += " Or removeMap is not actually removing maps."; 
      fail(msg);
    } catch (EmptySymTabException err) { }
  }

  /**
   * Map addition inserts 1 map at front of linked list
   */
  public void testSymTab_addMap() {
    int nAdded = 3;
    String msg;

    for (int i = 0; i < nAdded; i++) {
      this.tab.addMap();
    }

    try {
      for (int i = 0; i < nAdded + 1; i++) {
        this.tab.removeMap();
      }
    } catch (EmptySymTabException err) {
      msg = "addMap failed to properly add maps to the symbol table."; 
      fail(msg);
    }

    try {
      this.tab.removeMap();
      msg = "addMap added too many maps to the symbol table.";
      msg += "  Or removeMap is not actually removing maps."; 
      fail(msg);
    } catch (EmptySymTabException err) { }
  }

  /**
   * Symbols are inserted properly into first map in list.
   * 
   * Checks that [Duplicate/EmptySymTab]Exception are thrown appropriately.
   * 
   */
  public void testSymTab_insert() {
    String msg;

    try {
      // insert 2 symbols into table
      try {
        this.tab.insert(this.name1, this.sym1);
        this.tab.insert(this.name2, this.sym2);
      } catch (DuplicateException err) {
        msg = "Unexpected duplicates found for: ";
        msg += "this.name1=" + this.name1 + ", this.name2=" + this.name2;
        fail(msg);
      }

      // attempt a duplicate symbol insertion into table
      try {
        this.tab.insert(this.name2, this.sym2);
        msg = "Duplicate expected, but not found for: ";
        msg += "this.name2=" + this.name2;
        fail(msg);
      } catch (DuplicateException err) { }

      this.tab.removeMap();
    } catch (EmptySymTabException err) {
      fail("Symbol table is empty.");
    }

    try {
      this.tab.insert(this.name1, this.sym1);
      msg = "Expected EmptySymTabException when inserting";
      msg += " into symbol table with no hashmaps.";
      fail(msg);
    } catch (EmptySymTabException err) {
    } catch (DuplicateException err) {
      fail("Received DuplicateException instead of EmptySymTabException");
    }
  }

  /** Local symbol retrieval returns symbols/null appropriately.
   *
   * Checks that retrieved symbols are the same as corresponding inserted ones.
   * Confirms null is returned for cases of empty map list and symbol not
   * found.  Also confirms that addMap 'resets' the Map queue so the same
   * symbol can again be inserted.
   */
  public void testSymTab_localLookup() {
    String msg;

    // test for null returns
    assertEQ(this.tab.localLookup(name1), null, this.msgNull);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.localLookup(name1), null, this.msgNull);

    this.tab.addMap();

    try {
      this.tab.insert(name1, sym1);
      this.tab.insert(name2, sym2);
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.localLookup(name2), sym2, "");

    // test that addMap allows us to reinsert same symbol w/o being duplic
    this.tab.addMap();

    try {
      this.tab.insert(name2, sym2);
    } catch (DuplicateException err) {
      fail("Something is wrong with the way addMap and localLookup work.");
    } catch (Exception err) {
      fail(err.getMessage());
    }

    // test the insertions in first hashmap (pushed to second slot) indeed
    // do return to their proper first slot in the hashmap list of symbols
    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.localLookup(name1), sym1, "");
  }

  /** Global symbol retrieval returns symbols/null appropriately.
   *
   * Checks that retrieved symbols are the same as corresponding inserted ones.
   * Confirms null is returned for cases of empty map list and symbol not
   * found.  Also confirms that addMap has no effect on global symbol
   * searching.  Confirms removeMap on a to-be-searched for symbol results in a
   * null return.
   */
  public void testSymTab_globalLookup() {
    String msgExists = "Symbol should not exists but does.";

    // test for null returns
    assertEQ(this.tab.localLookup(name1), null, this.msgNull);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.localLookup(name1), null, this.msgNull);

    // test for non-null retrieval
    this.tab.addMap();
    try {
      this.tab.insert(name1, this.sym1);
      this.tab.addMap();
      this.tab.insert(name2, this.sym2);
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.globalLookup(name1), this.sym1, "");
    assertEQ(this.tab.globalLookup(name2), this.sym2, "");
    assertEQ(this.tab.globalLookup("qwerty"), null, msgExists);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertEQ(this.tab.globalLookup(name1), this.sym1, "");
    assertEQ(this.tab.globalLookup(name2), null, msgExists);
  }

  /**
   * Allows for a visual check of the SymTab print to stdout.
   */
  public void testSymTab_print() {
    this.tab.print();
  }
}
