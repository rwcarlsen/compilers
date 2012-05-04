###
# This Makefile can be used to make a parser for the Little language
# (parser.class) and to make a program (P5.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

CP = java_cup.jar:JLex.jar:.

FLAGS = -classpath ${CP}

JC = javac ${FLAGS}
JR = java ${FLAGS}

###
# build lexer, parser, lex and parse and generate code
###
all: P5.class
	java -classpath ${CP} P5 test.Little test.asm

###
# run generated code
###
run: all
	spim -file test.asm

P5.class: P5.java parser.class Yylex.class ASTnode.class
	$(JC) -g P5.java

parser.class: parser.java ASTnode.class Yylex.class Errors.class
	$(JC) parser.java

parser.java: Little.cup
	${JR} java_cup.Main < Little.cup

Yylex.class: Little.jlex.java sym.class Errors.class
	$(JC) Little.jlex.java

ASTnode.class: ast.java SymTab.class
	$(JC) -g ast.java

SymTab.class: SymTab.java Sym.class
	$(JC) -g SymTab.java

Sym.class: Sym.java
	$(JC) -g Sym.java

Little.jlex.java: Little.jlex sym.class
	${JR} JLex.Main Little.jlex

sym.class: sym.java
	$(JC) -g sym.java

sym.java: Little.cup
	${JR} java_cup.Main < Little.cup

Errors.class: Errors.java
	$(JC) Errors.java

###
# clean
###
clean:
	rm -f *~ *.class parser.java Little.jlex.java sym.java *.asm

