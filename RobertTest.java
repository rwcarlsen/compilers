

import java.lang.reflect.*;
import java.util.*;

/**
 * Superclass for test fixtures providing easy pass/fail tracking and
 * assertion functionality.
 * 
 * Each test method written by a subclass is run inside its own clean instance
 * of its fixture class (each test is independent). The main method of the
 * subclass-fixture runs the tests in it by invoking the static 'RobertTest.go'
 * method.
 */
public class RobertTest extends Object {

  private static Map<String, String> colors;

  private int testCount;
  private int failCount;
  private int passCount;

  private boolean passed;
  
  private ArrayList<String> tmpErrs;
  private ArrayList<StackTraceElement[]> traces;


  /** initialize pass/fail counts and error/traces lists. */
  public RobertTest() {
    this.testCount = 0;
    this.failCount = 0;
    this.passCount = 0;

    this.passed = true;

    this.tmpErrs = new ArrayList<String>();
    this.traces = new ArrayList<StackTraceElement[]>();
  }

  /** Used to initiate testing on the specified class.
   * 
   * @param subname class name of the RobertTest subclass fixture
   * 
   */
  public static void go(String subname) {
    colors = new HashMap<String, String>();
    colors.put("reset", "0");
    colors.put("black", "30");
    colors.put("red", "31");
    colors.put("green", "32");
    colors.put("yellow", "33");
    colors.put("blue", "34");
    colors.put("magenta", "35");
    colors.put("cyan", "36");
    colors.put("white", "37");

    RobertTest test = new RobertTest();
    test.runTests(subname);
  }

  /** invokes each test method in the specified class in its own sandbox */
  private void runTests(String subname) {
    System.out.print("\r\n");

    try {
      Object arglist[] = new Object[0];
      Class<?> c = Class.forName(subname);
      RobertTest t;

      Method[] allMethods = c.getDeclaredMethods();
      for (Method m : allMethods) {
        setColor("reset");
        t = (RobertTest)c.newInstance();
        String mname = m.getName();
        if (!mname.startsWith("test")) {
          continue;
        }

        this.testCount += 1;
        try {
          m.setAccessible(true);
          m.invoke(t, arglist);
          if (t.passed) {
            t.printPassed(mname);
            this.passCount += 1;
          } else {
            t.printFailed(mname);
            this.failCount += 1;
          }
        } catch (InvocationTargetException x) {
          Throwable cause = x.getCause();
          setColor("red");
          System.out.format("Invocation of %s failed: %s%n",
              mname, cause.getMessage());
          setColor("reset");
          for (StackTraceElement elem : x.getStackTrace()) {
            System.out.println("    " + elem);
          }
        }
      }
    } catch (Exception err) {
      System.out.println(err);
      err.printStackTrace();
    }
    printFinal();
  }

  /** Prints a passed message for the given test.*/
  private void printPassed(String methodName) {
    setColor("green");
    System.out.println(methodName.replace("test", "") + ": PASSED");
  }

  /** Prints a failed message for the given test including
   * a detail message (if specified) and a stack trace.
   */
  private void printFailed(String methodName) {
    setColor("red");
    System.out.println(methodName.replace("test", "") + ": FAILED");
    for (int i = 0; i < this.tmpErrs.size(); i++) {
      setColor("reset");
      System.out.println("    * detail: " + this.tmpErrs.get(i));

      System.out.println("        stack-trace:");
      StackTraceElement[] trace = this.traces.get(i);
      for (int j = 1; j < trace.length; j++) {
        System.out.println("            " + trace[j]);
      }
    }
    System.out.print("\r\n");

    this.tmpErrs.clear();
    this.traces.clear();
  }

  /** Prints summary statistics for the test fixture and its tests. */
  private void printFinal() {
    int noRunCount = this.testCount - this.passCount - this.failCount;

    setColor("yellow");
    System.out.println("\r\nSummary:");

    setColor("reset");
    System.out.print("    ");
    System.out.print(testCount);
    System.out.print(" tests total.\r\n");

    if (noRunCount > 0) {
      setColor("red");
      System.out.print("    ");
      System.out.print(noRunCount);
      System.out.print(" tests failed to run.\r\n");
    }

    if (this.failCount > 0) {
      setColor("red");
      System.out.print("    ");
      System.out.print(this.failCount);
      System.out.print(" tests failed.\r\n");
    } 

    setColor("green");
    System.out.print("    ");
    System.out.print(this.passCount);
    System.out.print(" tests passed.\r\n");

    if (this.failCount == 0 && noRunCount == 0) {
      setColor("green");
      System.out.print("    ");
      System.out.print("All tests passed.\r\n");
    }

    System.out.print("\r\n");
    setColor("reset");
  }

  /** Changes terminal text color using ascii escape sequences.*/
  private static void setColor(String color) {
    String attrib = "1;";
    if (color == "reset") {
      attrib = "";
    }

    System.out.print("\033[" + attrib + colors.get(color) + "m");
  }

  /** Assert the passed boolean val/expression is true.
   *
   * An entire test fails (not the entire fixture) if any single assertion fails.
   * The test method will continue normal execution even if the assertion fails.
   */
  public void assertTrue(boolean val, String msg) {
    if (!val) {
      this.passed = false;
      this.tmpErrs.add("Expected 'true', received 'false'. " + msg);

      StackTraceElement[] elements = new Throwable().getStackTrace();
      this.traces.add(elements);
    }
  }

  /** Assert the passed boolean val/expression is true.
   *
   * Provides more detailed output than 'assertTrue' by printing the two values
   * if they are not equal.
   * An entire test fails (not the entire fixture) if any single assertion fails.
   * The test method will continue normal execution even if the assertion fails.
   */
  public void assertEQ(Object obj1, Object obj2, String msg) {
    if (obj1 != obj2) {
      this.passed = false;
      this.tmpErrs.add("'" + obj1 + "' != '" + obj2 + "'. " + msg);

      StackTraceElement[] elements = new Throwable().getStackTrace();
      this.traces.add(elements);
    }
  }

  /** Manually force an individual test to fail by invoking anywhere
   *  in the test method.
   */
  public void fail(String msg) {
    this.passed = false;
    this.tmpErrs.add("Manual fail. " + msg);

    StackTraceElement[] elements = new Throwable().getStackTrace();
    this.traces.add(elements);
  }

}
