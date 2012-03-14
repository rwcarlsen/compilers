###
# This Makefile can be used to make a parser for the Little language
# (parser.class) and to make a program (P3.class) that tests the parser and
# the unparse methods in ast.java.
#
# make clean removes all generated files.
#
###

CP = lib:lib/java_cup.jar:lib/JLex.jar:.

FLAGS = -classpath ${CP}

JC = javac ${FLAGS}
JR = java ${FLAGS}

P3.class: P3.java parser.class Yylex.class ASTnode.class
	$(JC) -g P3.java

parser.class: parser.java ASTnode.class Yylex.class Errors.class
	$(JC) parser.java

parser.java: Little.cup
	${JR} java_cup.Main < Little.cup

Yylex.class: Little.jlex.java sym.class Errors.class
	$(JC) Little.jlex.java

ASTnode.class: ast.java
	$(JC) -g ast.java

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
	rm -f *~ *.class parser.java Little.jlex.java sym.java

###
# run provided parser
###
test:
	java -classpath testclass:${CP} P3 test.Little test.out

###
# run my parser
###
run:
	java -classpath ${CP} P3 test.Little test.out
