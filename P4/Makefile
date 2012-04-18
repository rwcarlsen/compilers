###
# This Makefile can be used to make a parser for the Little language
# (parser.class) and to make a program (P4.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

CP = java_cup.jar:JLex.jar:.

FLAGS = -classpath ${CP}

JC = javac ${FLAGS}
JR = java ${FLAGS}

P4.class: P4.java parser.class Yylex.class ASTnode.class
	$(JC) -g P4.java

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
	rm -f *~ *.class parser.java Little.jlex.java sym.java *.out

###
# run provided parser
###
test:
	java -classpath testclass:${CP} P4 test.Little test.out

###
# run my parser
###
run: P4.class
	java -classpath ${CP} P4 test.Little test.out

runerr: P4.class
	java -classpath ${CP} P4 nameErrors.Little test.out

