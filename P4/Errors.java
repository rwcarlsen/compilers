// Error
//
// This class is used to generate warning and fatal error messages.

class Errors {
  public static boolean kickedTheBucket;

  static {
    kickedTheBucket = false;
  }

  static void fatal(int lineNum, int charNum, String msg) {
    System.err.println(lineNum + ":" + charNum + " **ERROR** " + msg);
    kickedTheBucket = true;
  }

  static void warn(int lineNum, int charNum, String msg) {
    System.err.println(lineNum + ":" + charNum + " **WARNING** " + msg);
  }
}
