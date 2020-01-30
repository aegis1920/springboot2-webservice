#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

# 프록시 설정 변경
function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc # 하나의 문장을 만들어 파이프라인으로 넘겨주기 위해서 echo. tee 명령어를 통해 변경할 프록시 주소를 출력함과 동시에 생성하고 .inc에 덮어쓴다.
    echo "> 엔진엑스 Reload"
    sudo service nginx reload # 엔진엑스 설정을 다시 불러온다. 끊김없이 불러와 restart와 다르다. 그래서 중요한 설정들은 반영되지 않는다. service-url은 외부 설정파일이라 reload로 가능하다.
}