#!/bin/bash - 
#===============================================================================
#
#          FILE:  server.sh
# 
#         USAGE:  ./server.sh 
# 
#   DESCRIPTION:  
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  (dianping), @dianping.com
#       COMPANY: www.dianping.com
#       CREATED: 10/21/2013 08:12:00 PM CST
#      REVISION:  ---
#===============================================================================

set -o nounset                              # Treat unset variables as an error

PIDFILE="server.pid"
case $1 in
start)
        nohup java -classpath dependency/*:.:play-jetty-server-1.0.jar jetty.hiJetty.App>> jetty-server.log &
        pid=$!
        sleep 5
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
echo -n "Stopping jetty server ... "
if [ ! -f "$PIDFILE" ]
then
        echo "no jetty server to stop (could not find file $PIDFILE)"
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
