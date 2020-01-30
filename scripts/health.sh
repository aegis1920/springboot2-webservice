#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

IDLE_PORT=$(find_idle_port)

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile "
sleep 10

for RETRY_COUNT in {1..10}
do
    RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
    UP_COUNT=$(echo ${RESPONSE} | grep 'real' | wc -l) # grep 'real'을 통해 RESPONSE에서 'real'문자열이 있는 곳을 알아내고 wc -l을 통해 'real'이라는 문자열의 라인 수를 알아낸다.

    if [ ${UP_COUNT} -ge 1 ]
    then # up_count >= 1을 통해 real 문자열이 있는지 검증
        echo "> Health Check 성공"
        switch_proxy
        break
    else
        echo "> Health Check의 응답을 알 수 없거나 혹은 실행 상태가 아닙니다."
        echo "> Health Check : ${RESPONSE}"
    fi

    if [ ${RETRY_COUNT} -eq 10 ]
    then
        echo "> Health Check 실패"
        echo "> 엔진엑스에 연결하지 않고 배포를 종료합니다."
        exit 1
    fi

    echo "> Health Check 연결 실패. 재시도..."
    sleep 10
done