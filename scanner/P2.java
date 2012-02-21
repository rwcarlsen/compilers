// This program is to be used to test the Little scanner.
// This version is set up to test all tokens, but more code is
// needed to test other aspects of the scanner (e.g., input that
// causes errors, character numbers, values associated with tokens).

import java.util.*;
import java.io.*;
import java.io.StringReader;
import java_cup.runtime.*;  // defines Symbol
import rwctest.*;

public class P2 extends RobertTest {
  public static void main(String[] args) { // may be thrown by yylex
    RobertTest.go("P2");
  }

  public void testGoodShortTokens() {
    String singles = "{}(),=;+-*/!<>&";
    String doubles = "++--&&||==!=<=>=";
    StringReader reader;
    ArrayList<String> results;
    String doub, single;

    for (int i = 0; i < singles.length(); i++) {
      results = new ArrayList<String>();
      single = singles.substring(i);
      if (i + 1 < singles.length()) {
        single = singles.substring(i, i + 1);
      }
      reader = new StringReader(single);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(single.equals(results.get(0)), results.get(0) + " != " + single);
    }

    for (int i = 0; i < doubles.length(); i += 2) {
      results = new ArrayList<String>();
      doub = doubles.substring(i);
      if (i + 2 < doubles.length()) {
        doub = doubles.substring(i, i + 2);
      }
      reader = new StringReader(doub);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(doub.equals(results.get(0)), doub + " != " + results.get(0));
    }
  }

  public void testGoodIdTokens() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> identifiers = new ArrayList<String>();

    identifiers.add("a");
    identifiers.add("abcde");
    identifiers.add("abc123");

    identifiers.add("abcde_");
    identifiers.add("_abcde");
    identifiers.add("ab_cde");

    identifiers.add("abcde__");
    identifiers.add("__abcde");
    identifiers.add("ab__cde");

    identifiers.add("_abc_de");
    identifiers.add("ab_c_de");
    identifiers.add("abc_de_");

    identifiers.add("_");
    identifiers.add("__");

    identifiers.add("abc_123");
    identifiers.add("abc_123_");
    identifiers.add("abc_12_3");

    identifiers.add("_abc_123");
    identifiers.add("abc__123");
    identifiers.add("abc__123_");

    String lit = "";
    for (int i = 0; i < 10; i++) {
      lit += "aaaaaaaaaaaaaaa";
    }
    identifiers.add(lit);

    for (String currID : identifiers) {
      results = new ArrayList<String>();
      reader = new StringReader(currID);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned for '" + currID + "' but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(currID.equals(results.get(0)), currID + " != " + results.get(0));
    }
  }

  public void testGoodStringLit() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();

    lits.add("\"\"");
    lits.add("\"1234567890\"");
    lits.add("\"!@#$%^&*()`~-_=+[]{}:;'<,>.?/\"");
    lits.add("\"abcdefghijklmnopqrstuvwxyz.\"");
    lits.add("\"abcd!@#$%$'{}/?123abc.\"");
    lits.add("\"''\"");
    lits.add("\"\\\"\"");
    lits.add("\"\\\\\"");
    lits.add("\"foo\\nbar\"");
    lits.add("\"\\t\"");
    lits.add("\"\\'\"");
    lits.add("\"%d\"");
    lits.add("\"%f\"");

    String lit = "\"";
    for (int i = 0; i < 10; i++) {
      lit += "aaaaaaaaaaaaaaa";
    }
    lits.add(lit + "\"");

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned for " + currLit + " but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(currLit.equals(results.get(0)), currLit + " != " + results.get(0));
    }
  }

  public void testGoodDoubleLit() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();
    Double lhs, rhs;

    lits.add("0.0");
    lits.add("0.0");
    lits.add("123.123");
    lits.add("123456789.123456789");

    lits.add(".0");
    lits.add("0.");
    lits.add("123.");
    lits.add(".123");
    lits.add(".0123");

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      rhs = new Double(results.get(0));
      lhs = new Double(currLit);
      assertTrue(lhs.compareTo(rhs) == 0, lhs.toString() + " != " + rhs.toString());
    }
  }

  public void testGoodIntLit() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();
    Integer lhs, rhs;

    lits.add("0");
    lits.add("1");
    int tot = 1;
    for (int i = 0; i < 31; i++) {
      tot *= 2;
      lits.add((new Integer(tot - 1)).toString());
    }
    lits.add((new Integer(Integer.MAX_VALUE - 1)).toString());
    lits.add((new Integer(Integer.MAX_VALUE)).toString());

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      rhs = new Integer(results.get(0));
      lhs = new Integer(currLit);
      //System.out.println("orig=" + lhs + ", lexed=" + rhs);
      assertTrue(lhs.compareTo(rhs) == 0, lhs.toString() + " != " + rhs.toString());
    }
  }

  public void testIllegalChars() {
    String singles = "`~@#$%^[]\\:'.?";
    StringReader reader;
    ArrayList<String> results;
    String single;

    for (int i = 0; i < singles.length(); i++) {
      results = new ArrayList<String>();
      single = singles.substring(i);
      if (i + 1 < singles.length()) {
        single = singles.substring(i, i + 1);
      }
      reader = new StringReader(single);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 0, 
        "Expected 0 tokens returned but got " + results.size() + " tokens: "
        + results.toString());
    }
  }

  public void testLineCount() {
    String text = "hello();\nif(i=1;i<10;i++)\n{\n  x = y + 3;\n  return x;\n}";
    StringReader reader = new StringReader(text);
    ArrayList<String> results = new ArrayList<String>();

    ArrayList<String> expected = new ArrayList<String>();;
    expected.add("hello");
    expected.add("(");
    expected.add(")");
    expected.add(";");
    expected.add("if");
    expected.add("i");
    expected.add("=");
    expected.add("1");
    expected.add(";");
    expected.add("i");
    expected.add("<");
    expected.add("10");
    expected.add(";");
    expected.add("i");
    expected.add("++");
    expected.add(")");
    expected.add("{");
    expected.add("x");
    expected.add("=");
    expected.add("y");
    expected.add("+");
    expected.add("3");
    expected.add(";");
    expected.add("return");
    expected.add("x");
    expected.add(";");
    expected.add("}");

    try {
      results = tokenTest(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " + results.size() + " tokens: "
      + results.toString());

  }

  public void testCharCount() {

  }

  public void testFormatTokens() {

  }

  public void testComments() {

  }

  public void testBadStringLits() {

  }

  public void testBadDoubleLits() {

  }

  public void testBadIntLits() {

  }

  // **********************************************************************
  // tokenTest
  //
  // open and read from file inTokens
  //
  // for each token read, write the corresponding string to inTokens.out
  //
  // if the input file contains all tokens, one per line, we can verify
  // correctness of the scanner by comparing the input and output files
  // **********************************************************************
  private static ArrayList<String> tokenTest(Reader inFile) throws IOException {
    CharNum.num = 1;
    // open input and output files
    PrintWriter outFile = null;
    ArrayList<String> lexemes = new ArrayList<String>();
    try {
      outFile = new PrintWriter(new FileWriter("tokens.out"));
    } catch (IOException ex) {
      System.err.println("tokens.out cannot be opened.");
      System.exit(-1);
    }

    // create and call the scanner
    Yylex scanner = new Yylex(inFile);
    Symbol token = scanner.next_token();
    String text;
    while (token.sym != sym.EOF) {
      text = stringForToken(token);
      lexemes.add(text);
      outFile.println(text);

      token = scanner.next_token();
    }
    outFile.close();
    return lexemes;
  }

  private static String stringForToken(Symbol token) {
    String lexeme = "";
    switch (token.sym) {
      case sym.INT:
        lexeme = "int";
        break;
      case sym.DBL:
        lexeme = "double";
        break;
      case sym.VOID:
        lexeme = "void";
        break;
      case sym.IF:
        lexeme = "if";
        break;
      case sym.ELSE:
        lexeme = "else";
        break;
      case sym.WHILE:
        lexeme = "while";
        break;
      case sym.RETURN:
        lexeme = "return";
        break;
      case sym.SCANF:
        lexeme = "scanf";
        break;
      case sym.PRINTF:
        lexeme = "printf";
        break;
      case sym.ID:
        lexeme = ((IdTokenVal)token.value).idVal;
        break;
      case sym.INTLITERAL:
        lexeme = Integer.toString(((IntLitTokenVal)token.value).intVal);
        break;
      case sym.DBLLITERAL:
        lexeme = Double.toString(((DblLitTokenVal)token.value).dblVal);
        break;
      case sym.STRINGLITERAL:
        lexeme = ((StrLitTokenVal)token.value).strVal;
        break;
      case sym.LCURLY:
        lexeme = "{";
        break;
      case sym.RCURLY:
        lexeme = "}";
        break;
      case sym.LPAREN:
        lexeme = "(";
        break;
      case sym.RPAREN:
        lexeme = ")";
        break;
      case sym.COMMA:
        lexeme = ",";
        break;
      case sym.ASSIGN:
        lexeme = "=";
        break;
      case sym.SEMICOLON:
        lexeme = ";";
        break;
      case sym.PLUS:
        lexeme = "+";
        break;
      case sym.MINUS:
        lexeme = "-";
        break;
      case sym.STAR:
        lexeme = "*";
        break;
      case sym.DIVIDE:
        lexeme = "/";
        break;
      case sym.PLUSPLUS:
        lexeme = "++";
        break;
      case sym.MINUSMINUS:
        lexeme = "--";
        break;
      case sym.NOT:
        lexeme = "!";
        break;
      case sym.AND:
        lexeme = "&&";
        break;
      case sym.OR:
        lexeme = "||";
        break;
      case sym.EQUALS:
        lexeme = "==";
        break;
      case sym.NOTEQUALS:
        lexeme = "!=";
        break;
      case sym.LESS:
        lexeme = "<";
        break;
      case sym.GREATER:
        lexeme = ">";
        break;
      case sym.LESSEQ:
        lexeme = "<=";
        break;
      case sym.GREATEREQ:
        lexeme = ">=";
        break;
      case sym.AMPERSAND:
        lexeme = "&";
        break;
      case sym.INT_FORMAT:
        lexeme = "\"%d\"";
        break;
      case sym.DBL_FORMAT:
        lexeme = "\"%f\"";
        break;
    }

    return lexeme;
  }
}
