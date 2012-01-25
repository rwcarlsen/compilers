
package rwctest;

import java.lang.reflect.*;
import java.util.*;

public class RobertTest extends Object {

  private int testCount;
  private int failCount;
  private int passCount;

  private boolean passed;
  
  private ArrayList<String> tmpErrs;

  public RobertTest() {
    this.testCount = 0;
    this.failCount = 0;
    this.passCount = 0;

    this.passed = true;

    this.tmpErrs = new ArrayList<String>();
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
            printPassed(mname);
            this.passCount += 1;
          } else {
            this.failCount += 1;
            printFailed(mname);
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
  }

  public boolean getPassed() {return this.passed;}

  protected void assertTrue(boolean val, String msg) {
    if (!val) {
      this.passed = false;
      tmpErrs.add(msg);
    }
  }

  private void printPassed(String methodName) {
    System.out.println("\033[1;32m" + methodName.replace("test", "") + ": PASSED");
  }

  private void printFailed(String methodName) {
    System.out.println("\033[31m" + methodName.replace("test", "") + ": FAILED");
    for (String err : this.tmpErrs) {
      System.out.println(err);
    }
    tmpErrs.clear();
  }
}
