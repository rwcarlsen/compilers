import java.io.*;
import java.util.*;

/* **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Little program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of children)
// or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//     FormalsListNode     list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        list of StmtNode
//     ExpListNode         list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       DblNode           -- none --
//       VoidNode          -- none --
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PreIncStmtNode      IdNode
//       PreDecStmtNode      IdNode
//       PostIncStmtNode     IdNode
//       PostDecStmtNode     IdNode
//       ReadIntStmtNode     IdNode
//       ReadDblStmtNode     IdNode
//       WriteIntStmtNode    ExpNode
//       WriteDblStmtNode    ExpNode
//       WriteStrStmtNode    ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       DblLitNode          -- none --
//       StringLitNode       -- none --
//       IdNode              -- none --
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode
//         UnaryMinusNode    ExpNode
//         NotNode           ExpNode
//         PlusPlusNode      IdNode
//         MinusMinusNode    IdNode
//       BinaryExpNode       ExpNode, ExpNode
//         AssignNode
//         PlusNode     
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with lists of kids, or internal
// nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   DblNode,  VoidNode,  IntLitNode,  DblLitNode, StringLitNode,
//        IdNode
//
// (2) Internal nodes with (possibly empty) lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//       ProgramNode,      VarDeclNode,     FnDeclNode,       FormalDeclNode,
//       FnBodyNode,       TypeNode,        AssignStmtNode,
//       PreIncStmtNode,   PreDecStmtNode,  PostIncStmtNode,  PostDecStmtNode,
//       ReadIntStmtNode,  ReadDblStmtNode, WriteIntStmtNode, WriteDblStmtNode
//       WriteStrStmtNode  IfStmtNode,      IfElseStmtNode,   WhileStmtNode,
//       CallStmtNode,     ReturnStmtNode,  CallExpNode,
//       UnaryExpNode,     BinaryExpNode,   UnaryMinusNode, NotNode,
//       AssignNode,       PlusNode,        MinusNode,      TimesNode,
//       DivideNode,       PlusPlusNode,    MinusMinusNode, AndNode,
//       OrNode,           EqualsNode,      NotEqualsNode,  LessNode,        
//       GreaterNode,      LessEqNode,      GreaterEqNode
//
// **********************************************************************/

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************
abstract class ASTnode { 
  // every subclass must provide an unparse operation
  abstract public void unparse(PrintWriter p, int indent);

  // this method can be used by the unparse methods to do indenting
  protected void doIndent(PrintWriter p, int indent) {
    for (int k=0; k<indent; k++) p.print("   ");
  }
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************
class ProgramNode extends ASTnode {

  private SymTab symtab;
  private DeclListNode myDeclList;

  public ProgramNode(DeclListNode L) {
    myDeclList = L;
    this.symtab = new SymTab();
  }

  public void unparse(PrintWriter p, int indent) {
    myDeclList.unparse(p, indent);
  }

  public void nanal() {
    symtab.addMap();

    try {
      myDeclList.nanal(symtab);
    } catch (EmptySymTabException err) {
      System.out.println("Name analsis is broken!!");
    }

    try {
      symtab.removeMap();
    } catch (EmptySymTabException err) {
      System.out.println("Name analsis is broken!!");
    }
  }
}

class DeclListNode extends ASTnode {
  // list of kids (DeclNodes)
  private List<DeclNode> myDecls;

  public DeclListNode(List<DeclNode> L) {
    myDecls = L;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    try {
      for (DeclNode oneDecl : myDecls) {
        oneDecl.unparse(p, indent);
        p.print("\n");
      }
    } catch (NoSuchElementException ex) {
      System.err.println("unexpected NoSuchElementException in DeclListNode.unparse");
      System.exit(-1);
    }
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    for (DeclNode decl : myDecls) {
      decl.nanal(symtab);
    }
  }
}

class FormalsListNode extends ASTnode {
  // list of kids (FormalDeclNodes)
  private List<FormalDeclNode> myFormals;

  public FormalsListNode(List<FormalDeclNode> L) {
    myFormals = L;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    for (FormalDeclNode node : myFormals) {
      node.unparse(p, 0);
      if (node != myFormals.get(myFormals.size() - 1)) {
        p.print(", ");
      }
    }
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    for (FormalDeclNode node : myFormals) {
      node.nanal(symtab);
    }
  }
}

class FnBodyNode extends ASTnode {
  // 2 kids
  private DeclListNode myDeclList;
  private StmtListNode myStmtList;

  public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
    myDeclList = declList;
    myStmtList = stmtList;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    myDeclList.unparse(p, indent);
    myStmtList.unparse(p, indent);
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myDeclList.nanal(symtab);
    myStmtList.nanal(symtab);
  }
}

class StmtListNode extends ASTnode {
  // list of kids (StmtNodes)
  private List<StmtNode> myStmts;

  public StmtListNode(List<StmtNode> L) {
    myStmts = L;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    for (StmtNode stmt : myStmts) {
      stmt.unparse(p, indent);
      p.print("\n");
    }
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    for (StmtNode stmt : myStmts) {
      stmt.nanal(symtab);
    }
  }
}

class ExpListNode extends ASTnode {
  public ExpListNode(List<ExpNode> L) {
    myExps = L;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    for (ExpNode exp : myExps) {
      exp.unparse(p, indent);
      if (exp != myExps.get(myExps.size() - 1)) {
        p.print(", ");
      }
    }
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    for (ExpNode node : myExps) {
      node.nanal(symtab);
    }
  }

  // list of kids (ExpNodes)
  private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************
abstract class DeclNode extends ASTnode {
  abstract public void nanal(SymTab symtab) throws EmptySymTabException;
}

class VarDeclNode extends DeclNode {
  // 2 kids
  private TypeNode myType;
  private IdNode myId;

  public VarDeclNode(TypeNode type, IdNode id) {
    myType = type;
    myId = id;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myType.unparse(p, 0);
    p.print(" ");
    myId.unparse(p, 0);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    if (myType.str == "void") {
      Errors.fatal(myId.ln, myId.ch, "Non-function declared void");
    }
    try {
      Sym sym = new Sym(myType.str, myId.str);
      symtab.insert(myId.str, sym);
    } catch (DuplicateException err) {
      Errors.fatal(myId.ln, myId.ch, "Multiply declared identifier");
    }
  }
}

class FnDeclNode extends DeclNode {
  // 4 kids
  private TypeNode myType;
  private IdNode myId;
  private FormalsListNode myFormalsList;
  private FnBodyNode myBody;

  public FnDeclNode(TypeNode type,
      IdNode id,
      FormalsListNode formalList,
      FnBodyNode body) {
    myType = type;
    myId = id;
    myFormalsList = formalList;
    myBody = body;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myType.unparse(p, 0);
    p.print(" ");
    myId.unparse(p, 0);
    p.print("(");
    myFormalsList.unparse(p, 0);
    p.print(")");
    
    p.print(" {\n");
    myBody.unparse(p, indent + 1);
    doIndent(p, indent);
    p.print("}");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    try {
      Sym sym = new Sym(myType.str, myId.str);
      symtab.insert(myId.str, sym);
    } catch (DuplicateException err) {
      Errors.fatal(myId.ln, myId.ch, "Multiply declared identifier");
    }
    symtab.addMap();
    myFormalsList.nanal(symtab);
    myBody.nanal(symtab);
    symtab.removeMap();
  }
}

class FormalDeclNode extends DeclNode {
  // 2 kids
  private TypeNode myType;
  private IdNode myId;

  public FormalDeclNode(TypeNode type, IdNode id) {
    myType = type;
    myId = id;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    myType.unparse(p, indent);
    p.print(" ");
    myId.unparse(p, indent);
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    if (myType.str == "void") {
      Errors.fatal(myId.ln, myId.ch, "Non-function declared void");
    }
    try {
      Sym sym = new Sym(myType.str, myId.str);
      symtab.insert(myId.str, sym);
    } catch (DuplicateException err) {
      Errors.fatal(myId.ln, myId.ch, "Multiply declared identifier");
    }
  }
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************
abstract class TypeNode extends ASTnode {
  public String str;

  public void unparse(PrintWriter p, int indent) {
    p.print(str);
  }
}

class IntNode extends TypeNode {
  public IntNode() {
    this.str = "int";
  }
}

class DblNode extends TypeNode {
  public DblNode() {
    this.str = "double";
  }
}

class VoidNode extends TypeNode {
  public VoidNode() {
    this.str = "void";
  }
}


// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
  public void nanal(SymTab symtab) throws EmptySymTabException { };
}

class AssignStmtNode extends StmtNode {
  // 1 kid
  private AssignNode myExp;

  public AssignStmtNode(AssignNode e) {
    myExp = e;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myExp.unparseNoParens(p, indent);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);
  }
}

class PreIncStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public PreIncStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("++");
    myId.unparse(p, indent);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class PreDecStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public PreDecStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("--");
    myId.unparse(p, indent);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class PostIncStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public PostIncStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myId.unparse(p, indent);
    p.print("++");
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class PostDecStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public PostDecStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myId.unparse(p, indent);
    p.print("--");
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class ReadIntStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public ReadIntStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("scanf(\"%d\", &");
    myId.unparse(p, indent);
    p.print(");");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class ReadDblStmtNode extends StmtNode {
  // 1 kid
  private IdNode myId;

  public ReadDblStmtNode(IdNode id) {
    myId = id;
  }

  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("scanf(\"%f\", &");
    myId.unparse(p, indent);
    p.print(");");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
  }
}

class WriteIntStmtNode extends StmtNode {
  // 1 kid
  private ExpNode myExp;

  public WriteIntStmtNode(ExpNode exp) {
    myExp = exp;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("printf(\"%d\", ");
    myExp.unparse(p, indent);
    p.print(");");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);
  }
}

class WriteDblStmtNode extends StmtNode {
  private ExpNode myExp;

  public WriteDblStmtNode(ExpNode exp) {
    myExp = exp;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("printf(\"%f\", ");
    myExp.unparse(p, indent);
    p.print(");");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);
  }
}

class WriteStrStmtNode extends StmtNode {
  private ExpNode myExp;

  public WriteStrStmtNode(ExpNode exp) {
    myExp = exp;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("printf(");
    myExp.unparse(p, indent);
    p.print(");");
  }
}

class IfStmtNode extends StmtNode {
  private ExpNode myExp;
  private DeclListNode myDeclList;
  private StmtListNode myStmtList;

  public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
    myDeclList = dlist;
    myExp = exp;
    myStmtList = slist;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("if (");
    myExp.unparse(p, 0);
    p.print(") {\n");
    myDeclList.unparse(p, indent + 1);
    myStmtList.unparse(p, indent + 1);
    doIndent(p, indent);
    p.print("}");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);

    symtab.addMap();
    myDeclList.nanal(symtab);
    myStmtList.nanal(symtab);
    symtab.removeMap();
  }
}

class IfElseStmtNode extends StmtNode {
  // 5 kids
  private ExpNode myExp;
  private DeclListNode myThenDeclList;
  private StmtListNode myThenStmtList;
  private StmtListNode myElseStmtList;
  private DeclListNode myElseDeclList;

  public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
      StmtListNode slist1, DeclListNode dlist2,
      StmtListNode slist2) {
    myExp = exp;
    myThenDeclList = dlist1;
    myThenStmtList = slist1;
    myElseDeclList = dlist2;
    myElseStmtList = slist2;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("if (");
    myExp.unparse(p, 0);
    p.print(") {\n");
    myThenDeclList.unparse(p, indent + 1);
    myThenStmtList.unparse(p, indent + 1);
    doIndent(p, indent);
    p.print("}\n");
    doIndent(p, indent);
    p.print("else {\n");
    myElseDeclList.unparse(p, indent + 1);
    myElseStmtList.unparse(p, indent + 1);
    doIndent(p, indent);
    p.print("}");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);

    symtab.addMap();
    myThenDeclList.nanal(symtab);
    myThenStmtList.nanal(symtab);
    symtab.removeMap();
    symtab.addMap();
    myElseDeclList.nanal(symtab);
    myElseStmtList.nanal(symtab);
    symtab.removeMap();
  }
}

class WhileStmtNode extends StmtNode {
  private ExpNode myExp;
  private DeclListNode myDeclList;
  private StmtListNode myStmtList;

  public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
    myExp = exp;
    myDeclList = dlist;
    myStmtList = slist;
  }
  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    p.print("while (");
    myExp.unparse(p, 0);
    p.print(") {\n");
    myDeclList.unparse(p, indent + 1);
    myStmtList.unparse(p, indent + 1);
    doIndent(p, indent);
    p.print("}");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);

    symtab.addMap();
    myDeclList.nanal(symtab);
    myStmtList.nanal(symtab);
    symtab.removeMap();
  }
}

class CallStmtNode extends StmtNode {
  private CallExpNode myCall;
  
  public CallStmtNode(CallExpNode call) {
    myCall = call;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    myCall.unparse(p, indent);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myCall.nanal(symtab);
  }
}

class ReturnStmtNode extends StmtNode {
  private ExpNode myExp;

  public ReturnStmtNode(ExpNode exp) {
    myExp = exp;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    doIndent(p, indent);
    if (myExp == null) {
      p.print("return;");
      return;
    }
    p.print("return ");
    myExp.unparse(p, indent);
    p.print(";");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);
  }
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
  public void nanal(SymTab symtab) throws EmptySymTabException { };
}

class IntLitNode extends ExpNode {
  private int myLineNum;
  private int myCharNum;
  private int myIntVal;

  public IntLitNode(int lineNum, int charNum, int intVal) {
    myLineNum = lineNum;
    myCharNum = charNum;
    myIntVal = intVal;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print(Integer.toString(myIntVal));
  }

}

class DblLitNode extends ExpNode {
  public DblLitNode(int lineNum, int charNum, double dblVal) {
    myLineNum = lineNum;
    myCharNum = charNum;
    myDblVal = dblVal;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print(Double.toString(myDblVal));
  }

  private int myLineNum;
  private int myCharNum;
  private double myDblVal;
}

class StringLitNode extends ExpNode {
  public StringLitNode(int lineNum, int charNum, String strVal) {
    myLineNum = lineNum;
    myCharNum = charNum;
    myStrVal = strVal;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print(myStrVal);
  }

  private int myLineNum;
  private int myCharNum;
  private String myStrVal;
}

class IdNode extends ExpNode {
  // fields
  public int ln;
  public int ch;
  public String str;
  public Sym sym;

  public IdNode(int lineNum, int charNum, String strVal) {
    ln = lineNum;
    ch = charNum;
    str = strVal;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print(str);
  }
}

class CallExpNode extends ExpNode {
  private IdNode myId;
  private ExpListNode myExpList;

  public CallExpNode(IdNode name, ExpListNode elist) {
    myId = name;
    myExpList = elist;
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    myId.unparse(p, indent);
    p.print("(");
    myExpList.unparse(p, indent);
    p.print(")");
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myId.str);
    if (sym == null) {
      Errors.fatal(myId.ln, myId.ch, "Undeclared identifier");
    } else {
      myId.sym = sym;
    }
    myExpList.nanal(symtab);
  }
}

abstract class UnaryExpNode extends ExpNode {
  protected ExpNode myExp;

  public UnaryExpNode(ExpNode exp) {
    myExp = exp;
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp.nanal(symtab);
  }
}

abstract class BinaryExpNode extends ExpNode {
  protected ExpNode myExp1;
  protected ExpNode myExp2;

  public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
    myExp1 = exp1;
    myExp2 = exp2;
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    myExp1.nanal(symtab);
    myExp2.nanal(symtab);
  }
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class PlusPlusNode extends UnaryExpNode {
  public PlusPlusNode(IdNode id) {
    super(id);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp.unparse(p, indent);
    p.print("++");
    p.print(")");
  }
}

class MinusMinusNode extends UnaryExpNode {
  public MinusMinusNode(IdNode id) {
    super(id);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp.unparse(p, indent);
    p.print("--");
    p.print(")");
  }
}

class UnaryMinusNode extends UnaryExpNode {
  public UnaryMinusNode(ExpNode exp) {
    super(exp);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    p.print("-");
    myExp.unparse(p, indent);
    p.print(")");
  }
}

class NotNode extends UnaryExpNode {
  public NotNode(ExpNode exp) {
    super(exp);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    p.print("!");
    myExp.unparse(p, indent);
    p.print(")");
  }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class AssignNode extends ExpNode {
  private IdNode myLhs;
  private ExpNode myExp;

  public AssignNode(IdNode lhs, ExpNode exp) {
    myLhs = lhs;
    myExp = exp;
  }

  // ** unparse **
  //
  // Two versions: One called from the unparse method of
  // AssignStmtNode -- do NOT enclose this assignment in parens;
  // The other called whenever this assignment really is an
  // expression, not a stmt, so DO enclose this assignment in
  // parens.

  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myLhs.unparse(p, indent);
    p.print(" = ");
    myExp.unparse(p, indent);
    p.print(")");
  }

  public void unparseNoParens(PrintWriter p, int indent) {
    myLhs.unparse(p, indent);
    p.print(" = ");
    myExp.unparse(p, indent);
  }

  public void nanal(SymTab symtab) throws EmptySymTabException {
    Sym sym = symtab.globalLookup(myLhs.str);
    if (sym == null) {
      Errors.fatal(myLhs.ln, myLhs.ch, "Undeclared identifier");
    } else {
      myLhs.sym = sym;
    }
    myExp.nanal(symtab);
  }
}

class PlusNode extends BinaryExpNode {
  public PlusNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" + ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class MinusNode extends BinaryExpNode {
  public MinusNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" - ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class TimesNode extends BinaryExpNode {
  public TimesNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" * ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class DivideNode extends BinaryExpNode {
  public DivideNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" / ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class AndNode extends BinaryExpNode {
  public AndNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" && ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class OrNode extends BinaryExpNode {
  public OrNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" || ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class EqualsNode extends BinaryExpNode {
  public EqualsNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" == ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class NotEqualsNode extends BinaryExpNode {
  public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" != ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class LessNode extends BinaryExpNode {
  public LessNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" < ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class GreaterNode extends BinaryExpNode {
  public GreaterNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" > ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class LessEqNode extends BinaryExpNode {
  public LessEqNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" <= ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

class GreaterEqNode extends BinaryExpNode {
  public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
    super(exp1, exp2);
  }

  // ** unparse **
  public void unparse(PrintWriter p, int indent) {
    p.print("(");
    myExp1.unparse(p, indent);
    p.print(" >= ");
    myExp2.unparse(p, indent);
    p.print(")");
  }
}

