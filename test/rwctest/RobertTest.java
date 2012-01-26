
package rwctest;

import java.lang.reflect.*;
import java.util.*;

public class RobertTest extends Object {

  private static Map<String, String> colors;

  private int testCount;
  private int failCount;
  private int passCount;

  private boolean passed;
  
  private ArrayList<String> tmpErrs;
  private ArrayList<StackTraceElement[]> traces;

  public RobertTest() {
    this.testCount = 0;
    this.failCount = 0;
    this.passCount = 0;

    this.passed = true;

    this.tmpErrs = new ArrayList<String>();
    this.traces = new ArrayList<StackTraceElement[]>();
  }

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

  private void runTests(String subname) {
    System.out.print("\r\n");

    try {
      Object arglist[] = new Object[0];
      Class<?> c = Class.forName(subname);
      RobertTest t;

      Method[] allMethods = c.getDeclaredMethods();
      for (Method m : allMethods) {
        t = (RobertTest)c.newInstance();
        String mname = m.getName();
        if (!mname.startsWith("test")) {
          continue;
        }

        this.testCount += 1;
        try {
          m.setAccessible(true);
          m.invoke(t, arglist);
          if (t.getPassed()) {
            t.printPassed(mname);
            this.passCount += 1;
          } else {
            t.printFailed(mname);
            this.failCount += 1;
          }
        } catch (InvocationTargetException x) {
          Throwable cause = x.getCause();
          System.out.format("invocation of %s failed: %s%n",
              mname, cause.getMessage());
        }
      }
    } catch (Exception err) {
      System.out.println(err);
      err.printStackTrace();
    }
    printFinal();
  }

  private boolean getPassed() {return this.passed;}

  private void printPassed(String methodName) {
    setColor("green");
    System.out.println(methodName.replace("test", "") + ": PASSED");
  }

  private void printFailed(String methodName) {
    setColor("red");
    System.out.println(methodName.replace("test", "") + ": FAILED");
    for (int i = 0; i < this.tmpErrs.size(); i++) {
      setColor("reset");
      System.out.println("    * expected 'true', received 'false'");
      System.out.println("        detail:" + this.tmpErrs.get(i));

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

  private void printFinal() {
    int noRunCount = this.testCount - this.passCount - this.failCount;

    setColor("yellow");
    System.out.println("\r\nSummary:");

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

  private static void setColor(String color) {
    String attrib = "1;";
    if (color == "reset") {
      attrib = "";
    }

    System.out.print("\033[" + attrib + colors.get(color) + "m");
  }

  public void assertTrue(boolean val, String msg) {
    if (!val) {
      this.passed = false;
      this.tmpErrs.add(msg);

      StackTraceElement[] elements = new Throwable().getStackTrace();
      this.traces.add(elements);
    }
  }

  public void assertNoThrow(boolean val, String msg) {
    if (!val) {
      this.passed = false;
      this.tmpErrs.add(msg);

      StackTraceElement[] elements = new Throwable().getStackTrace();
      this.traces.add(elements);
    }
  }
}
