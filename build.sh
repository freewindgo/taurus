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
PROFILE=online

rm -rf output
mkdir output

if [ $# -ne 0 ]; then
    PROFILE=$1
fi
if [ $PROFILE = test ]; then
    mvn clean compile package -U -P$PROFILE -Dmaven.test.skip=true #pmd:pmd findbugs:findbugs checkstyle:checkstyle pmd:cpd
else
    mvn clean compile package -U -P$PROFILE -Dmaven.test.skip=true
fi
check_error
cp ./target/*.jar output
cp src/main/resources/$PROFILE/application.* -r ./output/

java_env="special_java_env.sh.$PROFILE"
if [ $PROFILE = test ]; then
    java_env="special_java_env.sh"
fi
if [ -f bin/$java_env ]; then
    cp bin/$java_env bin/java_env.sh;
fi
cp bin -r ./output/

#unzip ./target/*.war -d output