import java.io.*;
import java.util.*;

// **********************************************************************
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
// **********************************************************************

// **********************************************************************
// ASTnode class (base class for all other kinds of nodes)
// **********************************************************************
abstract class ASTnode { 
    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void doIndent(PrintWriter p, int indent) {
	for (int k=0; k<indent; k++) p.print(" ");
    }
}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************
class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
	myDeclList = L;
    }

    public void unparse(PrintWriter p, int indent) {
	myDeclList.unparse(p, indent);
    }

    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> L) {
	myDecls = L;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
	try {
	    for (DeclNode oneDecl : myDecls) {
		oneDecl.unparse(p, indent);
	    }
	} catch (NoSuchElementException ex) {
	    System.err.println("unexpected NoSuchElementException in DeclListNode.unparse");
	    System.exit(-1);
	}
    }

    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> L) {
	myFormals = L;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
	myDeclList = declList;
	myStmtList = stmtList;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> L) {
	myStmts = L;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> L) {
	myExps = L;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************
abstract class DeclNode extends ASTnode {
}

class VarDeclNode extends DeclNode {
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
	p.println(";");
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class FnDeclNode extends DeclNode {
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
    }

    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
	myType = type;
	myId = id;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************
abstract class TypeNode extends ASTnode {
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
	p.print("int");
    }
}

class DblNode extends TypeNode {
    public DblNode() {
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}


// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode e) {
	myExp = e;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private AssignNode myExp;
}

class PreIncStmtNode extends StmtNode {
    public PreIncStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class PreDecStmtNode extends StmtNode {
    public PreDecStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class ReadIntStmtNode extends StmtNode {
    public ReadIntStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class ReadDblStmtNode extends StmtNode {
    public ReadDblStmtNode(IdNode id) {
	myId = id;
    }

    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private IdNode myId;
}

class WriteIntStmtNode extends StmtNode {
    public WriteIntStmtNode(ExpNode exp) {
	myExp = exp;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private ExpNode myExp;
}

class WriteDblStmtNode extends StmtNode {
    public WriteDblStmtNode(ExpNode exp) {
	myExp = exp;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private ExpNode myExp;
}

class WriteStrStmtNode extends StmtNode {
    public WriteStrStmtNode(ExpNode exp) {
	myExp = exp;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
	myDeclList = dlist;
	myExp = exp;
	myStmtList = slist;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
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
    }

    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
	myExp = exp;
	myDeclList = dlist;
	myStmtList = slist;
    }
    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
	myCall = call;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
	myExp = exp;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 1 kid
    private ExpNode myExp;
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
	myLineNum = lineNum;
	myCharNum = charNum;
	myIntVal = intVal;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class DblLitNode extends ExpNode {
    public DblLitNode(int lineNum, int charNum, double dblVal) {
	myLineNum = lineNum;
	myCharNum = charNum;
	myDblVal = dblVal;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
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
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
	myLineNum = lineNum;
	myCharNum = charNum;
	myStrVal = strVal;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
	p.print(myStrVal);
    }

    // fields
    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
	myId = name;
	myExpList = elist;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
	myExp = exp;
    }

    // one kid
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
	myExp1 = exp1;
	myExp2 = exp2;
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
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
    }
}

class MinusMinusNode extends UnaryExpNode {
    public MinusMinusNode(IdNode id) {
	super(id);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
	super(exp);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
	super(exp);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

class AssignNode extends ExpNode {
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
    }

    public void unparseNoParens(PrintWriter p, int indent) {
    }

    // 2 kids
    private IdNode myLhs;
    private ExpNode myExp;
}

class PlusNode extends BinaryExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class MinusNode extends BinaryExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class TimesNode extends BinaryExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class DivideNode extends BinaryExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class AndNode extends BinaryExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class OrNode extends BinaryExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class EqualsNode extends BinaryExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class NotEqualsNode extends BinaryExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class LessNode extends BinaryExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class GreaterNode extends BinaryExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class LessEqNode extends BinaryExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

class GreaterEqNode extends BinaryExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
	super(exp1, exp2);
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
    }
}

