#!/bin/bash
export JAVA_HOME=/root/jdk1.8/jdk1.8.0_25
export MAVEN_HOME=/root/software/apache-maven-3.5.0
export PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}

cd `dirname $0`
BUILD_DIR=`pwd`

echo $BUILD_DIR

function check_error()
{
    if [ ${?} -ne 0 ]
    then
        echo "Error! Please Check..."
        exit 1
    fi
}
PROFILE=dev

rm -rf output
mkdir output

if [ $# -ne 0 ]; then
    PROFILE=$1
fi
#

mvn clean compile install -U -P$PROFILE -Dmaven.test.skip=true
check_error


#mv ./target/*.war ./output/
