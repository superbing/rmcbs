#!/bin/bash
BASE_DIR=$(cd $(dirname $0)/..; pwd)
MYLIB=$BASE_DIR/lib
CLASSPATH=$BASE_DIR/conf:$MYLIB/*
echo $CLASSPATH
pid=`ps -ef|grep $CLASSPATH |grep -v grep |awk '{print \$2}'`
echo $pid
if [ -z $pid ]; then
	echo "server not start ..."
   else
	kill -9 $pid
	echo $pid " stop ok ..."
fi