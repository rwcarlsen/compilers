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

%{
  private int comment_count = 0;
%} 

ALPHA=[A-Za-z]
DIGIT=[0-9]
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\012]
WHITE_SPACE_CHAR=[\n\ \t\b\012]
STRING_TEXT=(\\\"|[^\n\"]|\\{WHITE_SPACE_CHAR}+\\)*
COMMENT_TEXT=([^/*\n]|[^*\n]"/"[^*\n]|[^/\n]"*"[^/\n]|"*"[^/\n]|"/"[^*\n])*
ILLEGAL_CHAR=[`~@#$%^':\.\?\\\]\[]

%implements java_cup.runtime.Scanner
%function next_token
%type java_cup.runtime.Symbol
%state COMMENT

%eofval{
return new Symbol(sym.EOF);
%eofval}

%line

%%

<YYINITIAL> \n {CharNum.num = 1;}

<YYINITIAL,COMMENT> . {
  //Errors.fatal(yyline + 1, CharNum.num,
  //         "unmatched input");
}

<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ {
  CharNum.num += yytext().length();
}

<YYINITIAL> {ILLEGAL_CHAR} {
  Errors.fatal(yyline + 1, CharNum.num,
       "ignoring illegal character: " + yytext());
  CharNum.num++;
}

<YYINITIAL> "int"  { 
  Symbol S = new Symbol(sym.INT,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}

<YYINITIAL> "void"  {
  Symbol S = new Symbol(sym.VOID,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "double"  {
  Symbol S = new Symbol(sym.DBL,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "if"  {
  Symbol S = new Symbol(sym.IF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "else"  {
  Symbol S = new Symbol(sym.ELSE,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "while"  {
  Symbol S = new Symbol(sym.WHILE,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "return"  {
  Symbol S = new Symbol(sym.RETURN,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "scanf"  {
  Symbol S = new Symbol(sym.SCANF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "printf"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}

<YYINITIAL> "{" {
  Symbol S = new Symbol(sym.LCURLY,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
}
<YYINITIAL> "}" {
  Symbol S = new Symbol(sym.RCURLY,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "(" {
  Symbol S = new Symbol(sym.LPAREN,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ")" {
  Symbol S = new Symbol(sym.RPAREN,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "," {
  Symbol S = new Symbol(sym.COMMA,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "=" {
  Symbol S = new Symbol(sym.ASSIGN,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ";" {
  Symbol S = new Symbol(sym.SEMICOLON,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "+" {
  Symbol S = new Symbol(sym.PLUS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "-" {
  Symbol S = new Symbol(sym.MINUS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "*" {
  Symbol S = new Symbol(sym.STAR,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "/" {
  Symbol S = new Symbol(sym.DIVIDE,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "++" {
  Symbol S = new Symbol(sym.PLUSPLUS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "--" {
  Symbol S = new Symbol(sym.MINUSMINUS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "!" {
  Symbol S = new Symbol(sym.NOT,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "&&"  {
  Symbol S = new Symbol(sym.AND,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "||"  {
  Symbol S = new Symbol(sym.OR,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "==" {
  Symbol S = new Symbol(sym.EQUALS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "!=" {
  Symbol S = new Symbol(sym.NOTEQUALS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "<"  {
  Symbol S = new Symbol(sym.LESS,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ">"  {
  Symbol S = new Symbol(sym.GREATER,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "<=" {
  Symbol S = new Symbol(sym.LESSEQ,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ">=" {
  Symbol S = new Symbol(sym.GREATEREQ,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "&"  {
  Symbol S = new Symbol(sym.AMPERSAND,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }

<YYINITIAL> {DIGIT}+ {// NOTE: the following computation of the integer value does NOT
      //       check for overflow.  This must be changed.
      int val = (new Integer(yytext())).intValue();
      Symbol S = new Symbol(sym.INTLITERAL,
                new IntLitTokenVal(yyline+1, CharNum.num, val)
         );
      CharNum.num += yytext().length();
      return S;
}

<YYINITIAL> \"{STRING_TEXT}\" {
  Symbol S = new Symbol(sym.STRINGLITERAL,
            new StrLitTokenVal(yyline + 1, CharNum.num, yytext()));

  CharNum.num += yytext().length();
	return S;
}
<YYINITIAL> \"{STRING_TEXT} {
  Errors.fatal(yyline + 1, CharNum.num,
       "ignoring unterminated string literal");
} 

<YYINITIAL,COMMENT> \n {
  CharNum.num = 1;
}

<YYINITIAL> "/*" { yybegin(COMMENT); comment_count += 1; }

<COMMENT> "/*" { comment_count += 1; }
<COMMENT> "*/" { 
	comment_count -= 1; 
	if (comment_count == 0) {
    yybegin(YYINITIAL); 
  }
}
<COMMENT> {COMMENT_TEXT} { }

