#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app/step3
PROJECT_NAME=springboot2-webservice

echo "> Build 파일 복사"
echo "> cp $REPOSITORY/zip/*.jar $REPOSITORY/"

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1) #  여러 jar 파일에서 가장 최신파일을 JAR_NAME 변수로 넣어준다. -t는 가장 최근 파일, -r은 내림차순

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행 권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME 를 profile=$IDLE_PROFILE 로 실행합니다."


# `-Dspring.config.location=classpath:/application.properties`
#- 스프링의 설정 파일 위치를 지정
#- classpath가 붙으면 jar 안에 있는 resources 디렉토리를 기준으로 경로가 생성
#- application-oauth.properties는 절대 경로를 사용한다. 외부에 파일이 있기 때문에.

# `-Dspring.profiles.active=real`
#- application-real.properties를 활성화 시킴
#- 안에 있는 real-db 역시 포함됨.

nohup java -jar \
        -Dspring.config.location=classpath:/application.properties,classpath:/application-$IDLE_PROFILE.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties \
        -Dspring.profiles.active=$IDLE_PROFILE \
        $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
