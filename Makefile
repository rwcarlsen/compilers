
FLAGS=-Xlint:unchecked

CLASSPATH=classes
SRC_DIR=src
LIB_DIR=lib

MAIN=SymTest.java

all: init ${MAIN}
	

${MAIN}: 
	
	javac -sourcepath ${SRC_DIR} -classpath ${CLASSPATH}:${LIB_DIR} \
				-d ${CLASSPATH} ${FLAGS} ${SRC_DIR}/${MAIN}

init:
	
	mkdir -p ${CLASSPATH}

clean:
	
	rm -Rf ${CLASSPATH}

