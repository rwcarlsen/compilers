
package rwctest;

import java.lang.reflect.*;
import java.util.*;

public class RobertTest extends Object {

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
    RobertTest test = new RobertTest();
    test.runTests(subname);
  }

  private void runTests(String subname) {

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

  private void printFinal() {
    int noRunCount = this.testCount - this.passCount - this.failCount;

    System.out.print("\033[1;33m");
    System.out.println("\r\nSummary:");

    if (noRunCount > 0) {
      System.out.print("\033[1;31m");
      System.out.print("    ");
      System.out.print(noRunCount);
      System.out.print(" tests failed to run.\r\n");
    }

    if (this.failCount > 0) {
      System.out.print("\033[1;31m");
      System.out.print("    ");
      System.out.print(this.failCount);
      System.out.print(" tests failed.\r\n");
    } 

    System.out.print("\033[1;32m");
    System.out.print("    ");
    System.out.print(this.passCount);
    System.out.print(" tests passed.\r\n");

    if (this.failCount == 0 && noRunCount == 0) {
      System.out.print("\033[1;32m");
      System.out.print("    ");
      System.out.print("All tests passed.\r\n");
    }

    System.out.print("\033[0m");
  }

  private void printPassed(String methodName) {
    System.out.println("\033[1;32m" + methodName.replace("test", "") + ": PASSED");
    System.out.print("\033[0m");
  }

  private void printFailed(String methodName) {
    System.out.println("\033[1;31m" + methodName.replace("test", "") + ": FAILED");
    for (int i = 0; i < this.tmpErrs.size(); i++) {
      System.out.println("\033[0m" +"    " + this.tmpErrs.get(i));

      System.out.println("    stack-trace:");
      for (StackTraceElement elem : this.traces.get(i)) {
        System.out.println("        " + elem);
      }
    }
    this.tmpErrs.clear();
    this.traces.clear();
  }

  public void assertTrue(boolean val, String msg) {
    if (!val) {
      this.passed = false;
      this.tmpErrs.add(msg);

      StackTraceElement[] elements = new Throwable().getStackTrace();
      this.traces.add(elements);
    }
  }
}
