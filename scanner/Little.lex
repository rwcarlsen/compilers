import java_cup.runtime.*; // defines the Symbol class

// The generated scanner will return a Symbol for each token that it finds.
// A Symbol contains an Object field named value; that field will be of type
// TokenVal, defined below.
//
// A TokenVal object contains the line number on which the token occurs as
// well as the number of the character on that line that starts the token.
// Some tokens (literals and IDs) also include the value of the token.

class TokenVal {
 // fields
    int linenum;
    int charnum;
 // constructor
    TokenVal(int l, int c) {
        linenum = l;
  charnum = c;
    }
}

class IntLitTokenVal extends TokenVal {
 // new field: the value of the integer literal
    int intVal;
 // constructor
    IntLitTokenVal(int l, int c, int val) {
        super(l,c);
  intVal = val;
    }
}

class DblLitTokenVal extends TokenVal {
 // new field: the value of the double literal
    double dblVal;
 // constructor
    DblLitTokenVal(int l, int c, double val) {
        super(l,c);
  dblVal = val;
    }
}

class IdTokenVal extends TokenVal {
 // new field: the value of the identifier
    String idVal;
 // constructor
    IdTokenVal(int l, int c, String val) {
        super(l,c);
  idVal = val;
    }
}

class StrLitTokenVal extends TokenVal {
 // new field: the value of the string literal
    String strVal;
 // constructor
    StrLitTokenVal(int l, int c, String val) {
        super(l,c);
  strVal = val;
    }
}

// The following class is used to keep track of the character number at which
// the current token starts on its line.
class CharNum {
  static int num=1;
}
%%

ALPHA=[A-Za-z]
DIGIT=[0-9]
NONEWLINE_WHITESPACE=[\ \t]
WHITESPACE=[\n\ \t]
STRING_TEXT=(\\\"|[^\n\"]|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^/*\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])*

// The next 3 lines are included so that we can use the generated scanner
// with java CUP (the Java parser generator)
%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol

// Tell JLex what to do on end-of-file
%eofval{
return new Symbol(sym.EOF);
%eofval}

// Turn on line counting
%line

%%

{DIGIT}+   {// NOTE: the following computation of the integer value does NOT
      //       check for overflow.  This must be changed.
      int val = (new Integer(yytext())).intValue();
      Symbol S = new Symbol(sym.INTLITERAL,
                new IntLitTokenVal(yyline+1, CharNum.num, val)
         );
      CharNum.num += yytext().length();
      return S;
     }

\n     {CharNum.num = 1;}

{WHITESPACE}+  {CharNum.num += yytext().length();}

"+"     {Symbol S = new Symbol(sym.PLUS, new TokenVal(yyline+1, CharNum.num));
      CharNum.num++;
      return S;
     }
      
.     {Errors.fatal(yyline+1, CharNum.num,
       "ignoring illegal character: " + yytext());
      CharNum.num++;
     }
