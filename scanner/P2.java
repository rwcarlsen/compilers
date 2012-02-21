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
        results = makeLexemes(reader);
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
        results = makeLexemes(reader);
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
        results = makeLexemes(reader);
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

  public void testKeywords() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<Symbol> tokens;
    ArrayList<String> keywords = new ArrayList<String>();

    keywords.add("int");
    keywords.add("void");
    keywords.add("double");
    keywords.add("if");
    keywords.add("else");
    keywords.add("while");
    keywords.add("return");
    keywords.add("scanf");
    keywords.add("printf");

    Integer lhs, rhs;
    for (String currKeyword : keywords) {
      results = new ArrayList<String>();
      tokens = new ArrayList<Symbol>();
      reader = new StringReader(currKeyword);
      reader2 = new StringReader(currKeyword + "cheese");
      try {
        results = makeLexemes(reader);
        tokens = makeTokens(reader2);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned for '" + currKeyword + "' but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(currKeyword.equals(results.get(0)), currKeyword + " != " + results.get(0));

      lhs = new Integer(currKeyword.length() + 1);
      rhs = new Integer(((TokenVal)(tokens.get(1).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString());
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
        results = makeLexemes(reader);
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
        results = makeLexemes(reader);
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
    for (int i = 0; i < 30; i++) {
      tot *= 2;
      lits.add((new Integer(tot)).toString());
    }
    lits.add((new Integer(Integer.MAX_VALUE - 1)).toString());
    lits.add((new Integer(Integer.MAX_VALUE)).toString());

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
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
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 0, 
        "Expected 0 lexemes returned but got " + results.size() + " lexemes: "
        + results.toString());
    }
  }

  private String makeCodeFrag() {
    String text = "hello();\n\tif(i=1;i<10;i++)\n\t{\n  x = y + 3;\n\t\treturn \"cheese\";\n\n}";
    return text;
  }

  private ArrayList<Integer> makeFragLineNums() {
    ArrayList<Integer> lines = new ArrayList<Integer>();

    lines.add(new Integer(1));
    lines.add(new Integer(1));
    lines.add(new Integer(1));
    lines.add(new Integer(1));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(2));
    lines.add(new Integer(3));
    lines.add(new Integer(4));
    lines.add(new Integer(4));
    lines.add(new Integer(4));
    lines.add(new Integer(4));
    lines.add(new Integer(4));
    lines.add(new Integer(4));
    lines.add(new Integer(5));
    lines.add(new Integer(5));
    lines.add(new Integer(5));
    lines.add(new Integer(7));

    return lines;
  }

  private ArrayList<Integer> makeFragCharNums() {
    ArrayList<Integer> chars = new ArrayList<Integer>();

    chars.add(new Integer(1));
    chars.add(new Integer(6));
    chars.add(new Integer(7));
    chars.add(new Integer(8));
    chars.add(new Integer(2));
    chars.add(new Integer(4));
    chars.add(new Integer(5));
    chars.add(new Integer(6));
    chars.add(new Integer(7));
    chars.add(new Integer(8));
    chars.add(new Integer(9));
    chars.add(new Integer(10));
    chars.add(new Integer(11));
    chars.add(new Integer(13));
    chars.add(new Integer(14));
    chars.add(new Integer(15));
    chars.add(new Integer(16));
    chars.add(new Integer(17));
    chars.add(new Integer(2));
    chars.add(new Integer(3));
    chars.add(new Integer(5));
    chars.add(new Integer(7));
    chars.add(new Integer(9));
    chars.add(new Integer(11));
    chars.add(new Integer(12));
    chars.add(new Integer(5));
    chars.add(new Integer(12));
    chars.add(new Integer(18));

    return chars;
  }

  private ArrayList<String> makeFragLexemes() {
    ArrayList<String> lexemes = new ArrayList<String>();

    lexemes.add("hello");
    lexemes.add("(");
    lexemes.add(")");
    lexemes.add(";");
    lexemes.add("if");
    lexemes.add("(");
    lexemes.add("i");
    lexemes.add("=");
    lexemes.add("1");
    lexemes.add(";");
    lexemes.add("i");
    lexemes.add("<");
    lexemes.add("10");
    lexemes.add(";");
    lexemes.add("i");
    lexemes.add("++");
    lexemes.add(")");
    lexemes.add("{");
    lexemes.add("x");
    lexemes.add("=");
    lexemes.add("y");
    lexemes.add("+");
    lexemes.add("3");
    lexemes.add(";");
    lexemes.add("return");
    lexemes.add("\"cheese\"");
    lexemes.add(";");
    lexemes.add("}");

    return lexemes;
  }

  public void testCodFrag() {
    ArrayList<String> expected = makeFragLexemes();
    String text = makeCodeFrag();

    StringReader reader = new StringReader(text);
    ArrayList<String> results = new ArrayList<String>();

    try {
      results = makeLexemes(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " lexemes returned but got " 
      + results.size() + " lexemes: " + results.toString());

    String lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = results.get(i);
      assertTrue(lhs.equals(rhs), lhs + " != " + rhs);
    }
  }

  public void testLineCount() {
    ArrayList<Integer> expected = makeFragLineNums();
    String text = makeCodeFrag();

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).linenum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString());
    }
  }

  public void testWhitespaceCharCount() {
    ArrayList<Integer> expected = new ArrayList<Integer>();
    String text = "\t +";
    expected.add(new Integer(3));

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testIdCharCount() {
    ArrayList<Integer> expected = new ArrayList<Integer>();
    String text = "cheese+cheese";
    expected.add(new Integer(1));
    expected.add(new Integer(7));
    expected.add(new Integer(8));

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testStringLitCharCount() {
    ArrayList<Integer> expected = new ArrayList<Integer>();
    String text = "\"hello\";";
    expected.add(new Integer(1));
    expected.add(new Integer(8));

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testDoubleLitCharCount() {
    ArrayList<String> textList = new ArrayList<String>();
    textList.add("1234.+cheese");
    textList.add(".1234+cheese");
    textList.add("12.34+cheese");

    ArrayList<Integer> expected = new ArrayList<Integer>();
    expected.add(new Integer(1));
    expected.add(new Integer(6));
    expected.add(new Integer(7));

    for (String currText : textList) {
      StringReader reader = new StringReader(currText);
      ArrayList<Symbol> results = new ArrayList<Symbol>();

      try {
        results = makeTokens(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == expected.size(), 
        "Expected " + expected.size() + " tokens returned but got " +
        results.size() + " tokens.");
      Integer lhs, rhs;
      for (int i = 0; i < expected.size(); i++) {
        lhs = expected.get(i);
        rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
        assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
            + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
      }
    }
  }

  public void testIntLitCharCount() {
    String currText = "1234567+cheese";

    ArrayList<Integer> expected = new ArrayList<Integer>();
    expected.add(new Integer(1));
    expected.add(new Integer(8));
    expected.add(new Integer(9));

    StringReader reader = new StringReader(currText);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testShortTokenCharCount() {
    String singles = "{}(),=;+-*/!<>&";
    String doubles = "++--&&||==!=<=>=";
    StringReader reader;
    ArrayList<Symbol> results;
    String doub, single;
    Integer lhs, rhs;

    for (int i = 0; i < singles.length(); i++) {
      results = new ArrayList<Symbol>();
      single = singles.substring(i);
      if (i + 1 < singles.length()) {
        single = singles.substring(i, i + 1);
      }
      reader = new StringReader(single + "cheese");
      try {
        results = makeTokens(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 2, 
        "Expected 2 tokens returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 2) {continue;}
      lhs = new Integer(2);
      rhs = new Integer(((TokenVal)(results.get(1).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString());
    }

    for (int i = 0; i < doubles.length(); i += 2) {
      results = new ArrayList<Symbol>();
      doub = doubles.substring(i);
      if (i + 2 < doubles.length()) {
        doub = doubles.substring(i, i + 2);
      }
      reader = new StringReader(doub + "cheese");
      try {
        results = makeTokens(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 2, 
        "Expected 2 tokens returned but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 2) {continue;}
      lhs = new Integer(3);
      rhs = new Integer(((TokenVal)(results.get(1).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString());
    }
  }

  public void DISABLED_testMultilineCharCount() {
    ArrayList<Integer> expected = makeFragCharNums();
    String text = makeCodeFrag();

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(((TokenVal)(results.get(i).value)).charnum);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testFormatTokens() {
    ArrayList<Integer> expected = new ArrayList<Integer>();
    String text = "\"%d\" \"%f\"";
    expected.add(new Integer(sym.INT_FORMAT));
    expected.add(new Integer(sym.DBL_FORMAT));

    StringReader reader = new StringReader(text);
    ArrayList<Symbol> results = new ArrayList<Symbol>();

    try {
      results = makeTokens(reader);
    } catch (IOException err) {
      fail("");
    }
    assertTrue(results.size() == expected.size(), 
      "Expected " + expected.size() + " tokens returned but got " +
      results.size() + " tokens.");
    Integer lhs, rhs;
    for (int i = 0; i < expected.size(); i++) {
      lhs = expected.get(i);
      rhs = new Integer(results.get(i).sym);
      assertTrue(lhs.equals(rhs), lhs.toString() + " != " + rhs.toString()
          + " for token " + (new Integer(i + 1)).toString() + ": " + stringForToken(results.get(i)));
    }
  }

  public void testGoodComments() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();

    // test comment by itself
    lits.add("/* hello */");
    lits.add("/*hello*/");
    lits.add("/*/* hello */*/");

    lits.add("/* \nhello */");
    lits.add("/*hello\n*/");
    lits.add("/*\nhello\n*/");
    lits.add("/*\n   hello\n*/");

    lits.add("   /*hello*/");
    lits.add("/*hello*/   ");

    
    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 0, 
        "Expected 0 tokens for '" + currLit + "' but got " + results.size() + " tokens: "
        + results.toString());
    }

    // test comment with tokens before/after
    lits = new ArrayList<String>();
    lits.add("/*hello*/myid");
    lits.add("/*hello*/\nmyid");
    lits.add("/*hello*/\n myid");

    lits.add("myid/*hello*/");
    lits.add("myid\n/*hello*/");
    lits.add("myid \n/*hello*/");

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token for '" + currLit + "' but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      assertTrue(results.get(0).equals("myid"), "myid != " + results.get(0));
    }
  }

  public void testBadComments() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();

    lits.add("/* hello");
    lits.add("/* hello\n");
    lits.add("/* /* hello");
    lits.add("/* /* hello\n");
    lits.add("/* /* foo */ hello");
    lits.add("/* /* foo */ hello\n");

    int count = 1;
    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      //assertTrue(results.size() == 1, 
      //  "Expected 1 token for lit" + (new Integer(count++)).toString() +
      //  "='" + currLit + "' but got " + results.size() + " tokens: "
      //  + results.toString());
    }
  }

  public void testBadStringLits() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();

    lits.add("\"bad");
    lits.add("\"bad\n");
    lits.add("\"bad\\a\"");
    lits.add("\"bad\\ \"");
    lits.add("\"very-bad\\ ");

    int count = 1;
    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 0, 
        "Expected 0 tokens for lit" + (new Integer(count++)).toString() +
        "='" + currLit + "' but got " + results.size() + " tokens: "
        + results.toString());
    }

  }

  public void testBadDoubleLits() {

  }

  public void testBadIntLits() {
    StringReader reader;
    ArrayList<String> results;
    ArrayList<String> lits = new ArrayList<String>();
    Integer rhs;

    Integer max = new Integer(Integer.MAX_VALUE);
    String maxVal = max.toString();
    lits.add("1" + maxVal);
    String big = (new Long((long)Integer.MAX_VALUE + (long)1)).toString();
    lits.add(big);
    
    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = makeLexemes(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token for '" + currLit + "' but got " + results.size() + " tokens: "
        + results.toString());
      if (results.size() != 1) {continue;}
      rhs = new Integer(results.get(0));
      //System.out.println("orig=" + lhs + ", lexed=" + rhs);
      assertTrue(max.compareTo(rhs) == 0, maxVal + " != " + rhs.toString());
    }
  }

  // **********************************************************************
  // makeLexemes
  //
  // open and read from file inTokens
  //
  // for each token read, write the corresponding string to inTokens.out
  //
  // if the input file contains all tokens, one per line, we can verify
  // correctness of the scanner by comparing the input and output files
  // **********************************************************************
  private static ArrayList<String> makeLexemes(Reader inFile) throws IOException {
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

  private static ArrayList<Symbol> makeTokens(Reader inFile) throws IOException {
    CharNum.num = 1;
    ArrayList<Symbol> tokens = new ArrayList<Symbol>();

    // create and call the scanner
    Yylex scanner = new Yylex(inFile);
    Symbol token = scanner.next_token();
    while (token.sym != sym.EOF) {
      tokens.add(token);
      token = scanner.next_token();
    }
    return tokens;
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
