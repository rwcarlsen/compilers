###
# This Makefile can be used to make a scanner for the Little language
# (Yylex.class) and to make a program that tests the scanner (P2.class).
#
# The default makes both the scanner and the test program.
#
# make clean removes all generated files.
#
# Note: P2.java will not compile unless Yylex.class exists.
#
###

# define the java compiler to be used and the flags
JC = javac
FLAGS = -g

P2.class: P2.java Yylex.class sym.class
	$(JC) $(FLAGS) P2.java

Yylex.class: Little.jlex.java Errors.class sym.class
	$(JC) $(FLAGS) Little.jlex.java

Little.jlex.java: Little.jlex sym.class
	java JLex.Main Little.jlex

sym.class: sym.java
	$(JC) $(FLAGS) sym.java

Errors.class: Errors.java
	$(JC) $(FLAGS) Errors.java

###
# clean up
###

clean:
	rm -f *~ *.class Little.jlex.java
