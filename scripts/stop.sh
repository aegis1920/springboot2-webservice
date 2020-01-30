#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH) # 현재 stop.sh가 속해있는 경로를 찾음. profile.sh의 경로를 찾기 위해서
source ${ABSDIR}/profile.sh # import구문과 같다.

IDLE_PORT=$(find_idle_port)

echo "> $IDLE_PORT 에서 구동중인 애플리케이션 pid 확인"
IDLE_PID=$(lsof -ti tcp:${IDLE_PORT}) # i옵션을 통해 TCP만, t옵션을 통해 pid만 가져온다.

if [ -z ${IDLE_PID} ] # 만약 빈 스트링이라면 true
then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $IDLE_PID"
    kill -15 ${IDLE_PID}
    sleep 5
fi