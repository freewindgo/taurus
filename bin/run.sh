#!/bin/bash
# @Date: 2015/06/11
# Usage: sh run.sh start|stop

#
cd $(dirname $0)
export BIN_DIR=$(pwd)
cd $BIN_DIR
cd ..
DEPLOY_DIR=`pwd`
MODULE_NAME=$(echo $DEPLOY_DIR | awk -F'/' '{print $NF}')
CONF_DIR=$DEPLOY_DIR/conf
#LIB_DIR=$DEPLOY_DIR/lib
LOG_DIR=$DEPLOY_DIR/logs
TEMP_DIR=$DEPLOY_DIR/temp
mkdir -p $TEMP_DIR

# loading system environment
if [ -f $BIN_DIR/java_env.sh ]; then
    source $BIN_DIR/java_env.sh
fi


if [ -f $BIN_DIR/parse_yaml.sh ]; then
    source $BIN_DIR/parse_yaml.sh
fi

if [ -f $BIN_DIR/webserver_env.sh ]; then
    source $BIN_DIR/webserver_env.sh
fi
# tomcat enviroment
if [ -f $BIN_DIR/tomcat_env.sh ]; then
    service_type="tomcat"
    source $BIN_DIR/tomcat_env.sh
    CLASS_PATH=${CATALINA_HOME}/bin/bootstrap.jar:${CATALINA_HOME}/bin/tomcat-juli.jar
    STDOUT_FILE=$LOG_DIR/catalina.out
    start_completely_flag="(Server start succ|Tomcat started on port|Server started succ)"
    REAL_LOG_DIR=$CATALINA_HOME/logs
else
    service_type="java_only"
    LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
    CLASS_PATH=$CONF_DIR:$LIB_JARS
    STDOUT_FILE=$LOG_DIR/test.out
    start_completely_flag="(Server start succ|Tomcat started on port|Server started succ)"
    REAL_LOG_DIR=/home/work/var/$MODULE_NAME/logs
fi

JAR_BIN=`ls ${BIN_DIR}/../*.jar`
if [ "${JAR_BIN}_X" == "_X" ];then
    echo "jar bin file should exists in ${BIN_DIR}/../"
    exit 2
fi

RUNJAVA="$JAVA_HOME/bin/java"
GC_LOG=$LOG_DIR/gc.log

# jvm config
export JAVA_BASE_OPTS=" -Djava.awt.headless=true -Dfile.encoding=utf-8 -Djava.io.tmpdir=$TEMP_DIR"
#export JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=${jmx_port}"
export JAVA_MEM_OPTS=" -server -Xms${java_initail_heap_size} -Xmx${java_max_heap_size} -Xmn${java_young_heap_size} -Xss256K "
export JAVA_GC_OPTS=" -verbose:gc -Xloggc:$GC_LOG -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+DisableExplicitGC"

export JAVA_CP=" -classpath $CLASS_PATH "
JAVA_OPTS="$JAVA_BASE_OPTS $JAVA_JMX_OPTS $JAVA_MEM_OPTS $JAVA_GC_OPTS"
EXEC_PID_FILE="$LOG_DIR/instance.pid"

# open: 0
# close: 1
#function is_port_closed() {
#    local port=$1
#    netstat -nl 2>/dev/null | grep -w "$port" | grep "LISTEN" &>/dev/null 2>&1
#    return $?
#}

# check if process is exist
# exist: return 1
function is_process_exist() {
    local pid=$(ps axuf | grep -Pi "${RUNJAVA}.*${DEPLOY_DIR}" | grep -v grep | awk '{print $2}')

    if [ "$pid"x = x ]; then
        return 0
    else
        return 1
    fi
}

# check start log
function check_start_log() {
    local line_num=0
    local retry_time=0
    local TOTAL_RETRY_TIME=60
    local sleep_time=5

    while (( 1 ))
    do
        if [ $retry_time -ge $TOTAL_RETRY_TIME ]; then
            return 2
        fi
        is_process_exist
        if [ $? -eq 0 ]; then
            echo "java process is exit!!"
            return 2
        fi
        eval line_num=$(grep -i -n -E "$start_completely_flag" $STDOUT_FILE | cut -d':' -f1|head -1)
        if [ "${line_num}"x == x ]; then
            echo -ne "."
            sleep $sleep_time && let "retry_time += 1"
        else
            head -n $line_num $STDOUT_FILE | grep " ERROR \| FATAL " >>/dev/null 2>&1
            return $?
        fi
    done
}

filemove() {
    if [ -f "$1" ]; then
        mv $1 $1.`date +%Y%m%d.%H%M`
    fi
}

function start_service()
{
    # make logs symlink
    if [ ! -L $LOG_DIR ]; then
        mkdir -p $REAL_LOG_DIR
        ln -s $REAL_LOG_DIR $LOG_DIR
    fi
    filemove $STDOUT_FILE
    filemove $GC_LOG

    if [ "$service_type"x = "tomcat"x ]; then
        nohup $RUNJAVA $SERVICE_EXTRA_OPTS $JAVA_OPTS $CATALINA_OPTS $MAIN_CLASS start >> $STDOUT_FILE 2>&1 &
        echo -n $! > $EXEC_PID_FILE
    elif [ "$service_type"x = "java_only"x ]; then
        #$RUNJAVA $SERVICE_EXTRA_OPTS $JAVA_OPTS $MAIN_CLASS >> $STDOUT_FILE 2>&1 &
        nohup $RUNJAVA $SERVICE_EXTRA_OPTS $JAVA_OPTS -jar ${JAR_BIN} >> $STDOUT_FILE 2>&1 &
        echo -n $! > $EXEC_PID_FILE
    else
        echo "Please specify the service type"
        exit 1
    fi
}

function stop_service()
{
    stop_http_server
    if [ -f "$EXEC_PID_FILE" ]; then
        EXEC_PID=$(cat $EXEC_PID_FILE)
    else
        EXEC_PID=$(ps axuf | grep ${DEPLOY_DIR} | grep -v grep | awk '{print $2}')
    fi
    kill $EXEC_PID > /dev/null 2>&1
    COUNT=`ps --no-heading -C java -f --width 2000 | grep "$DEPLOY_DIR" | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        sleep 5
        kill -9 $EXEC_PID > /dev/null 2>&1
    fi
    rm -f $EXEC_PID_FILE
}

function start() {
    echo "Starting..."
    local sleep_time=1
    local retry_time=0
    local TOTAL_RETRY_TIME=5

    is_process_exist
    if [ $? -eq 1 ]; then
        echo "already started!"
        exit 0;
    fi

    start_service
    while (( $retry_time < $TOTAL_RETRY_TIME ))
    do
        sleep $sleep_time
        sleep $sleep_time
        is_process_exist
        if [ $? -ne 1 ]; then
            (( retry_time += 1))
            echo -ne "."
    else
        break
        fi
    done
    echo

    if [ $retry_time -ge $TOTAL_RETRY_TIME ]; then
        echo "Error: process stop, failed!"
        exit 1
    else
        echo "Info: process start."
    fi

    if [ -n $STDOUT_FILE ]; then
        echo "Info: checking startup log..."
        check_start_log
        flag=$?
        if [ $flag -eq 1 ]; then
            echo "Started!"
	elif [ $flag -eq 2 ]; then
	    echo "check timeout,please do it by yourself ...."
            exit 1;
        else
            echo "Error: have exception log, exit"
            exit 1;
        fi
    fi
}

function stop() {
    echo "Stoping..."
    is_process_exist
    if [ $? -eq 0 ]; then
        echo "already stoped!"
        return
    fi

    stop_service
}

function restart() {
    local pid=$(ps axuf | grep ${DEPLOY_DIR} | grep -v grep | awk '{print $2}')
    if [ "$pid"x != x ]; then
        # thread dump
        ${JAVA_HOME}/bin/jstack $pid > ${LOG_DIR}/thread.dump.$(date +%Y%m%d.%H%M)
        stop_service
    fi
    start_service
}


function stop_http_server {
  local shutdown_url=$(get_shutdown_url "${BIN_DIR}/../application.yml")
  echo "shutdown url:$shutdown_url"
  curl -X POST "$shutdown_url"
  echo ""
  echo "wait a moment ......."
  sleep 6
}

case $1 in
start)
    start;;
stop)
    stop;;
restart)
    restart;;
*)
    echo "Usage: sh $0 {start|stop|restart}"
    exit 1
    ;;
esac
