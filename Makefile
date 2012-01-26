
FLAGS=-Xlint:unchecked

CLASSPATH=classes
JUNIT_LIB=junit-4.10.jar
SRC_DIR=src
LIB_DIR=lib

SRC_LIST=src-list.txt

all: init
	
	javac -sourcepath ${SRC_DIR} -classpath ${CLASSPATH}:${LIB_DIR}/${JUNIT_LIB} \
		@${SRC_LIST} -d ${CLASSPATH} ${FLAGS}

init:
	
	mkdir -p ${CLASSPATH}

clean:
	
	rm -Rf ${CLASSPATH}

