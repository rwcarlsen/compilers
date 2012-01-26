
import rwccompiler.*;
import rwctest.*;

public class SymTest extends RobertTest {

  public static void main(String[] args) {
    RobertTest.go("SymTest");
  }

  public void testDefaultConstructor() {
    String name = "foopid";
    Sym mysym = new Sym(name);
    
    String msg = "name=" + name + ", getType()=" + mysym.getType();
    assertTrue(mysym.getType() == name, msg);
    assertTrue(mysym.getType() == name, msg);
  }

  public void testGetType() {
    assertTrue(false, "needs implementation");
  }

  public void testToString() {
    assertTrue(false, "needs implementation");
  }
}
