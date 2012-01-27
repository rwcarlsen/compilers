
import java.util.*;

import rwccompiler.*;
import rwctest.*;

public class SymTest extends RobertTest {

  private SymTab tab;

  private String type1;
  private String name1;
  private Sym sym1;

  private String type2;
  private String name2;
  private Sym sym2;

  private String msgBadRetrieve, msgNull;

  /**
   * initialize the test fixture
   */
  public SymTest() {
    this.tab = new SymTab();

    this.name1 = "";
    this.type1 = "monkey";
    this.sym1 = new Sym(this.type1);

    this.type2 = "elephant";
    this.name2 = "foopid";
    this.sym2 = new Sym(this.type2);

    this.msgBadRetrieve = "symbol '" + name2 + "' was not retrieved successfully.";
    this.msgNull = "Expected null and received something else.";
  }

  /**
   * initiate and run all tests in this file
   */
  public static void main(String[] args) {

    RobertTest.go("SymTest");
  }

//---------------------------------------------------------------------------//
//------------------------- Sym tests ---------------------------------------//
//---------------------------------------------------------------------------//

  /**
   * Sym class def constructor sets type correctly.
   */
  public void testSym_constructor() {
    String msg;
    
    msg = "name:" + this.type1 + " != getType():" + this.sym1.getType();
    assertTrue(this.sym1.getType() == this.type1, msg);

    msg = "name:" + this.type2 + " != getType():" + this.sym2.getType();
    assertTrue(this.sym2.getType() == this.type2, msg);

    // repeat to be sure this method doesn't modify the object's type
    assertTrue(this.sym1.getType() == this.type1, msg);
  }

  /**
   * Sym.toString method builds proper string representation
   */
  public void testSym_toString() {
    
    String msg = "name:" + this.type2 + " != getType():" + this.sym2.getType();
    assertTrue(this.sym2.toString() == this.type2, msg);

    // repeat to be sure this method doesn't modify the object's type
    assertTrue(this.sym2.toString() == this.type2, msg);
  }

//---------------------------------------------------------------------------//
//-------------------------- SymTab tests -----------------------------------//
//---------------------------------------------------------------------------//

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

//---------------------------------------------------------------------------//

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

//---------------------------------------------------------------------------//

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

//---------------------------------------------------------------------------//

  public void testSymTab_localLookup() {
    String msg;

    // test for null returns
    assertTrue(this.tab.localLookup(name1) == null, this.msgNull);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertTrue(this.tab.localLookup(name1) == null, this.msgNull);

    this.tab.addMap();

    try {
      this.tab.insert(name1, sym1);
      this.tab.insert(name2, sym2);
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertTrue(this.tab.localLookup(name2) == sym2, this.msgBadRetrieve);

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

    assertTrue(this.tab.localLookup(name1) == sym1, this.msgBadRetrieve);
  }

//---------------------------------------------------------------------------//

  public void testSymTab_globalLookup() {
    String msgExists;
    msgExists = "symbol '" + name2 + "' should not exists but does.";

    // test for null returns
    assertTrue(this.tab.localLookup(name1) == null, this.msgNull);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertTrue(this.tab.localLookup(name1) == null, this.msgNull);

    // test for non-null retrieval
    this.tab.addMap();
    try {
      this.tab.insert(name1, this.sym1);
      this.tab.addMap();
      this.tab.insert(name2, this.sym2);
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertTrue(this.tab.globalLookup(name1) == this.sym1, this.msgBadRetrieve);
    assertTrue(this.tab.globalLookup(name2) == this.sym2, this.msgBadRetrieve);
    assertTrue(this.tab.globalLookup("qwerty") == null, this.msgExists);

    try {
      this.tab.removeMap();
    } catch (Exception err) {
      fail(err.getMessage());
    }

    assertTrue(this.tab.globalLookup(name1) == this.sym1, this.msgBadRetrieve);
    assertTrue(this.tab.globalLookup(name2) == null, this.msgExists);
  }

//---------------------------------------------------------------------------//

  public void testSymTab_print() {

  }
}
