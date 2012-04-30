import java.io.*;
import java_cup.runtime.*;

// **********************************************************************
// Main program to test the Little parser.
//
// There should be 2 command-line arguments:
//    1. the file to be parsed
//    2. the output file into which the AST built by the parser should be
//       unparsed
// The program opens the two files, creates a scanner and a parser, and
// calls the parser.  If the parse is successful, the AST is unparsed.
// **********************************************************************

public class P4 {
  public static void main(String[] args) throws IOException {
    // check for command-line args
    if (args.length != 2) {
      System.err.println("please supply name of file to be parsed and name of file for unparsed version.");
      System.exit(-1);
    }

    // open input file
    FileReader inFile = null;
    try {
      inFile = new FileReader(args[0]);
    } catch (FileNotFoundException ex) {
      System.err.println("File " + args[0] + " not found.");
      System.exit(-1);
    }

    // create output file print writer in Codegen class
    try {
      Codegen.p = new PrintWriter(args[1]);
    } catch (FileNotFoundException ex) {
      System.err.println("File " + args[1] +
          " could not be opened for writing.");
      System.exit(-1);
    }

    // parse the darn thing
    parser P = new parser(new Yylex(inFile));
    Symbol root=null;
    try {
      root = P.parse(); // do the parse
      System.out.println ("program parsed correctly.");
    } catch (Exception ex){
      System.err.println("Exception occured during parse: " + ex);
      System.exit(-1);
    }

    ProgramNode top = (ProgramNode)root.value;

    if (Errors.kickedTheBucket) {
      System.out.println("lexer and/or parser found errors.");
      return;
    }

    // name analysis
    top.processNames();
    top.typeCheck();

    if (Errors.kickedTheBucket) {
      System.out.println("name analysis and/or type check found errors.");
      return;
    }

    top.codeGen();
    Codegen.p.close();

    return;
  }
}
