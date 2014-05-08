#!/bin/sh

PIDFILE="pid"

case "$1" in
  start)
	nohup ./manage.py runserver 0.0.0.0:8095 --noreload &
        pid=$!
    	kill -0 $pid
    	if [ $? -eq 0 ]
    	then
        	echo $pid > $PIDFILE
        	echo Started 
    	else
        	echo Start Error
        	exit 1
    	fi
	;;
  stop)
	SLEEP=5
	echo -n "Stopping server ... "
    	if [ ! -f "$PIDFILE" ]
    	then
      		echo "no server to stop (could not find file $PIDFILE)"
    	else
      		kill $(cat "$PIDFILE")
      	while [ $SLEEP -gt 0 ]; do
        	kill -0 $(cat "$PIDFILE") >/dev/null 2>&1
        	if [ $? -eq 1 ]; then
          		rm "$PIDFILE"
          		break
        	fi
        	if [ $SLEEP -gt 0 ]; then
          		sleep 1
        	fi
        	if [ $SLEEP -eq 0 ]; then
          		echo "fail to stop server"
          		exit 1
        	fi
        	SLEEP=`expr $SLEEP - 1 `
      	done
      echo STOPPED
    fi
    exit 0
        ;;
esac
exit 0
