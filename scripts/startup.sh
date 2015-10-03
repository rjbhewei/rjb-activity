#! /bin/bash

[ -z $LOGCENTER_HOME ] && LOGCENTER_HOME=`cd ..;pwd`
echo "LOGCENTER_HOME   : "$LOGCENTER_HOME

LOGCENTER_BIN=$LOGCENTER_HOME/bin
LOGCENTER_SERVER=$LOGCENTER_HOME/server
LOGCENTER_LIB=$LOGCENTER_HOME/lib
LOGCENTER_LOG=$LOGCENTER_HOME/logs
LOGCENTER_OPTS=$LOGCENTER_BIN/runtime.properties

LOGCENTER_PROJECT_FILE_PREFIX="logSearch-"
LOGCENTER_PROJECT_FILE_SUFFIX="jar"

if [ ! -z "$CLASSPATH" ]; then
  CLASSPATH="$CLASSPATH"":"
fi

if [ ! -d "$LOGCENTER_LOG" ]; then
  mkdir $LOGCENTER_LOG
fi
if [ -z "$LOGCENTER_OUT" ]; then
  LOGCENTER_OUT="$LOGCENTER_LOG"/logSearch.out
fi
touch "$LOGCENTER_OUT"
> "$LOGCENTER_OUT"

cd $LOGCENTER_SERVER
PROJECT_JAR=`ls -l $LOGCENTER_PROJECT_FILE_PREFIX*.$LOGCENTER_PROJECT_FILE_SUFFIX | grep '^-' | awk '{print $9}' | sort -V | awk 'END{print $1}'`
# TODO catch error
PROJECT_NAME=${PROJECT_JAR%$".$LOGCENTER_PROJECT_FILE_SUFFIX"}
if [ ! -d $LOGCENTER_SERVER/$PROJECT_NAME ]; then
  echo "extract $PROJECT_JAR"
  unzip -q $LOGCENTER_SERVER/$PROJECT_JAR -d $LOGCENTER_SERVER/$PROJECT_NAME
fi

CLASSPATH=$CLASSPATH$LOGCENTER_LIB/*:$LOGCENTER_SERVER/$PROJECT_NAME

JAVA_OPTS=""
while read line; do
  if [ -n "$line" -a "${line:0:1}" != "#" ]; then
    if [ -n "$JAVA_OPTS" ]; then
      JAVA_OPTS="$JAVA_OPTS $line"
    else
      JAVA_OPTS="$line"
    fi
  fi
done < "$LOGCENTER_OPTS"

echo "LOGCENTER_OUT    : $LOGCENTER_OUT"
echo "CLASSPATH : $CLASSPATH"
echo "JAVA_OPTS : $JAVA_OPTS"

cd $LOGCENTER_HOME
exec java -DlogSearch.home=$LOGCENTER_HOME -DlogSearch.running=$PROJECT_NAME -DlogSearch.env.ver=1.0 $JAVA_OPTS -cp $CLASSPATH com.hewei.Launcher >> "$LOGCENTER_OUT" 2>&1 &

