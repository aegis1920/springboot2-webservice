#!/usr/bin/env bash

# 쉬고 있는 profile 찾기: real1이 사용 중이면 real2가 쉬고 있고, 반대면 real1이 쉬고 있음

function find_idle_profile()
{
    # 현재 엔진엑스가 바라보고 있는 스프링 부트가 정상적으로 수행중인지 확인. 응답값이 HttpStatus로 받는다. 400 이상은 모두 예외로 real2를 현재 profile로
    # IDLE_PROFILE : 엔진엑스와 연결되지 않은 profile. 스프링 부트 프로젝트를 이 profile로 연결하기 위해 반환
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)
    if [ ${RESPONSE_CODE} -ge 400 ] # 400보다 크면
    then
        CURRENT_PROFILE=real2
    else
        CURRENT_PROFILE=$(curl -s http://localhost/profile)
    fi

    if [ ${CURRENT_PROFILE} == real1 ]
    then
        IDLE_PROFILE=real2
    else
        IDLE_PROFILE=real1
    fi

    echo "${IDLE_PROFILE}" # bash는 값을 반환하는 기능이 없어 결과를 출력 후 사용. 그래서 중간에 echo를 사용하면 안된다.
}

function find_idle_port()
{
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
        echo "8081"
    else
        echo "8082"
    fi
}