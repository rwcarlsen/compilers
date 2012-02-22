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
NONNEWLINE_WHITE_SPACE_CHAR=[\ \t\b\012]
WHITE_SPACE_CHAR=[\n\ \t\b\012]
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
      
"."     {Errors.fatal(yyline+1, CharNum.num,
       "ignoring illegal character: " + yytext());
      CharNum.num++;
     }

/////////////////////////////////////////////////////////
////////////// keyword tokens ///////////////////////////
/////////////////////////////////////////////////////////

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
  Symbol S = new Symbol(sym.DOUBLE,
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

/////////////////////////////////////////////////////////
////////////// one and two char tokens //////////////////
/////////////////////////////////////////////////////////

<YYINITIAL> "{" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "}" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "(" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ")" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "," {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "=" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ";" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "+" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "-" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "*" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "/" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "++" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "--" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "!" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "&&"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "||"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "==" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "!=" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "<"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ">"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "<=" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> ">=" {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }
<YYINITIAL> "&"  {
  Symbol S = new Symbol(sym.PRINTF,
            new TokenVal(yyline + 1, CharNum.num));

  CharNum.num += yytext().length();
  return S;
  }


<YYINITIAL> {NONNEWLINE_WHITE_SPACE_CHAR}+ { }

<YYINITIAL,COMMENT> \n { }

<YYINITIAL> "/*" { yybegin(COMMENT); comment_count = comment_count + 1; }

<COMMENT> "/*" { comment_count = comment_count + 1; }
<COMMENT> "*/" { 
	comment_count = comment_count - 1; 
	Utility.assertion(comment_count >= 0);
	if (comment_count == 0) {
    		yybegin(YYINITIAL);
	}
}
<COMMENT> {COMMENT_TEXT} { }

<YYINITIAL> \"{STRING_TEXT}\" {
	String str =  yytext().substring(1,yytext().length() - 1);
	
	Utility.assertion(str.length() == yytext().length() - 2);
	return (new Yytoken(40,str,yyline,yychar,yychar + str.length()));
}
<YYINITIAL> \"{STRING_TEXT} {
	String str =  yytext().substring(1,yytext().length());

	Utility.error(Utility.E_UNCLOSEDSTR);
	Utility.assertion(str.length() == yytext().length() - 1);
	return (new Yytoken(41,str,yyline,yychar,yychar + str.length()));
} 
<YYINITIAL> {DIGIT}+ { 
	return (new Yytoken(42,yytext(),yyline,yychar,yychar + yytext().length()));
}	
<YYINITIAL> {ALPHA}({ALPHA}|{DIGIT}|_)* {
	return (new Yytoken(43,yytext(),yyline,yychar,yychar + yytext().length()));
}	
<YYINITIAL,COMMENT> . {
        System.out.println("Illegal character: <" + yytext() + ">");
	Utility.error(Utility.E_UNMATCHED);
}
