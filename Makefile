
CLASSPATH=classes
JUNIT_LIB=junit.jar
SRC_DIR=src
LIB_DIR=lib

SRC_LIST=src-list.txt

all: init
	
	javac -sourcepath ${SRC_DIR} -classpath ${CLASSPATH}:${LIB_DIR}/${JUNIT_LIB} \
		@${SRC_LIST} -d ${CLASSPATH}

init: clean
	
	mkdir ${CLASSPATH}

clean:
	
	rm -Rf ${CLASSPATH}

