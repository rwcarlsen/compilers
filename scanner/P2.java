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
  public static void main(String[] args) throws IOException // may be thrown by yylex
  {
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
        "Expected 1 token returned but got " + results.size() + " tokens.");
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
        "Expected 1 token returned but got " + results.size() + " tokens.");
      if (results.size() != 1) {continue;}
      assertTrue(doub.equals(results.get(0)), results.get(0) + " != " + doub);
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

    for (String currID : identifiers) {
      results = new ArrayList<String>();
      reader = new StringReader(currID);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens.");
      if (results.size() != 1) {continue;}
      assertTrue(currID.equals(results.get(0)), results.get(0) + " != " + currID);
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

    for (String currLit : lits) {
      results = new ArrayList<String>();
      reader = new StringReader(currLit);
      try {
        results = tokenTest(reader);
      } catch (IOException err) {
        fail("");
      }
      assertTrue(results.size() == 1, 
        "Expected 1 token returned but got " + results.size() + " tokens.");
      if (results.size() != 1) {continue;}
      assertTrue(currLit.equals(results.get(0)), results.get(0) + " != " + currLit);
    }
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
    while (token.sym != sym.EOF) {
      switch (token.sym) {
        case sym.INT:
          outFile.println("int");
          lexemes.add("int");
          break;
        case sym.DBL:
          outFile.println("double");
          lexemes.add("double");
          break;
        case sym.VOID:
          outFile.println("void");
          lexemes.add("void");
          break;
        case sym.IF:
          outFile.println("if");
          lexemes.add("if");
          break;
        case sym.ELSE:
          outFile.println("else");
          lexemes.add("else");
          break;
        case sym.WHILE:
          outFile.println("while");
          lexemes.add("while");
          break;
        case sym.RETURN:
          outFile.println("return");
          lexemes.add("return");
          break;
        case sym.SCANF:
          outFile.println("scanf");
          lexemes.add("scanf");
          break;
        case sym.PRINTF:
          outFile.println("printf");
          lexemes.add("printf");
          break;
        case sym.ID:
          outFile.println(((IdTokenVal)token.value).idVal);
          lexemes.add(((IdTokenVal)token.value).idVal);
          break;
        case sym.INTLITERAL:
          outFile.println(((IntLitTokenVal)token.value).intVal);
          lexemes.add(Integer.toString(((IntLitTokenVal)token.value).intVal));
          break;
        case sym.DBLLITERAL:
          outFile.println(((DblLitTokenVal)token.value).dblVal);
          lexemes.add(Double.toString(((DblLitTokenVal)token.value).dblVal));
          break;
        case sym.STRINGLITERAL:
          outFile.println(((StrLitTokenVal)token.value).strVal);
          lexemes.add(((StrLitTokenVal)token.value).strVal);
          break;
        case sym.LCURLY:
          outFile.println("{");
          lexemes.add("{");
          break;
        case sym.RCURLY:
          outFile.println("}");
          lexemes.add("}");
          break;
        case sym.LPAREN:
          outFile.println("(");
          lexemes.add("(");
          break;
        case sym.RPAREN:
          outFile.println(")");
          lexemes.add(")");
          break;
        case sym.COMMA:
          outFile.println(",");
          lexemes.add(",");
          break;
        case sym.ASSIGN:
          outFile.println("=");
          lexemes.add("=");
          break;
        case sym.SEMICOLON:
          outFile.println(";");
          lexemes.add(";");
          break;
        case sym.PLUS:
          outFile.println("+");
          lexemes.add("+");
          break;
        case sym.MINUS:
          outFile.println("-");
          lexemes.add("-");
          break;
        case sym.STAR:
          outFile.println("*");
          lexemes.add("*");
          break;
        case sym.DIVIDE:
          outFile.println("/");
          lexemes.add("/");
          break;
        case sym.PLUSPLUS:
          outFile.println("++");
          lexemes.add("++");
          break;
        case sym.MINUSMINUS:
          outFile.println("--");
          lexemes.add("--");
          break;
        case sym.NOT:
          outFile.println("!");
          lexemes.add("!");
          break;
        case sym.AND:
          outFile.println("&&");
          lexemes.add("&&");
          break;
        case sym.OR:
          outFile.println("||");
          lexemes.add("||");
          break;
        case sym.EQUALS:
          outFile.println("==");
          lexemes.add("==");
          break;
        case sym.NOTEQUALS:
          outFile.println("!=");
          lexemes.add("!=");
          break;
        case sym.LESS:
          outFile.println("<");
          lexemes.add("<");
          break;
        case sym.GREATER:
          outFile.println(">");
          lexemes.add(">");
          break;
        case sym.LESSEQ:
          outFile.println("<=");
          lexemes.add("<=");
          break;
        case sym.GREATEREQ:
          outFile.println(">=");
          lexemes.add(">=");
          break;
        case sym.AMPERSAND:
          outFile.println("&");
          lexemes.add("&");
          break;
        case sym.INT_FORMAT:
          outFile.println("\"%d\"");
          lexemes.add("\"%d\"");
          break;
        case sym.DBL_FORMAT:
          outFile.println("\"%f\"");
          lexemes.add("\"%f\"");
          break;
      }

      token = scanner.next_token();
    }
    outFile.close();
    return lexemes;
  }
}
