
import rwccompiler.*;
import rwctest.*;

public class MyTests extends RobertTest {

  /**
   * initiate and run all tests in this file
   */
  public static void main(String[] args) {
    RobertTest.go("SymTest");
  }

  /**
   * Sym class def constructor sets type correctly.
   */
  public void testSym_constructor() {
    String name = "foopid";
    Sym mysym = new Sym(name);
    
    String msg = "name:" + name + " != getType():" + mysym.getType();
    assertTrue(mysym.getType() == name, msg);

    // repeat to be sure this method doesn't modify the object's type
    assertTrue(mysym.getType() == name, msg);
  }

  /**
   * Sym.toString method builds proper string representation
   */
  public void testSym_toString() {
    String name = "foopid";
    Sym mysym = new Sym(name);
    
    String msg = "name:" + name + " != getType():" + mysym.getType();
    assertTrue(mysym.toString() == name, msg);

    // repeat to be sure this method doesn't modify the object's type
    assertTrue(mysym.toString() == name, msg);
  }

  public void testSymTab_removeMap() {

  }

  public void testSymTab_insert() {

  }

  public void testSymTab_addMap() {

  }

  public void testSymTab_localLookup() {

  }

  public void testSymTab_globalLookup() {

  }

  public void testSymTab_print() {

  }
}
