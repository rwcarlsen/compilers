
MAIN=SymTest.java

CLASSPATH=classes
SRC_DIR=src
LIB_DIR=lib
DOC_DIR=doc

TEST_SUITE=rwctest
COMPILER_SUITE=rwccompiler

FLAGS=-Xlint:unchecked

###############################################################

all: init ${MAIN} doc
	

${MAIN}:
	
	javac -sourcepath ${SRC_DIR} -classpath ${CLASSPATH}:${LIB_DIR} \
				-d ${CLASSPATH} ${FLAGS} ${SRC_DIR}/${MAIN}

init:
	
	mkdir -p ${CLASSPATH}

clean:
	
	rm -Rf ${CLASSPATH}
	rm -Rf ${DOC_DIR}

doc:
	
	javadoc -sourcepath ${SRC_DIR} -d ${DOC_DIR} ${TEST_SUITE} ${COMPILER_SUITE} ${SRC_DIR}/${MAIN}

