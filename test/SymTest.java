
import rwccompiler.*;
import rwctest.*;

public class SymTest extends RobertTest {

  public static void main(String[] args) {
    RobertTest.go("SymTest");
  }

  public void testDefaultConstructor() {
    String msg;

    msg = "one failed";
    assertTrue(true, msg);
    msg = "two failed";
    assertTrue(false, msg);
    msg = "three failed";
    assertTrue(true, msg);
  }

  public void testGetType() {

  }

  public void testToString() {

  }
}
