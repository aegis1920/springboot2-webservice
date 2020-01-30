## 프로젝트 개요
이 프로젝트는 [이동욱 개발자](https://jojoldu.tistory.com/)님의 [스프링 부트와 AWS로 혼자 구현하는 웹 서비스 책](https://jojoldu.tistory.com/463)을 2주 정도의 기간을 거쳐 혼자 따라한 프로젝트입니다. 또한 여기에 적는 README 또한 혼자 공부하며 정리한 내용이며 코드에 대한 부분은 주석으로 모두 달아놓았습니다.

## 프로젝트 필사 및 정리

#### intellij
- 프로젝트를 처음 시작할 때 인덱싱을 다 해서 자료 검색 속도가 훨씬 빠르다고 한다.

#### Lombok
- Lombok을 쓰기 위해선 Preferences > Compiler > Annotation Processors > Enable annotaion processing 체크를 프로젝트마다 해줘야 함.
- 필수 어노테이션은 아니기에 어노테이션이 여러 개일 때 윗쪽에 써도 됨

#### JPA
- MyBatis, iBatis는 쿼리를 매핑하기때문에 SQL Mapper다.
- JPA는 객체를 매핑하기때문에 ORM(Object Relational Mapping)
- RDB는 데이터를 어떻게 저장할지에 초점이 맞춰진 기술이고 OOP는 메세지를 기반으로 기능과 속성을 한 곳에서 관리하는 기술. 서로 패러다임이 불일치하기에 JPA가 탄생했다.
- JPA는 이 패러다임을 일치 시켜주기 위한 기술. **JPA는 인터페이스로 구현체가 필요하다.** 예를 들어 Hibernate, Eclipse Link, Spring Data JPA가 있다. 
- Spring팀에서 Spring Data JPA를 권장하는 이유는 새로운 JPA 구현체가 나타났을 때 쉽게 교체할 수 있기 때문이다. 또한 새로운 저장소가 나타다너라도 의존성만 교체하면 된다. 예를 들어 Spring Data Redis(구현체), Spring Data MongoDB(저장소) 등. 그래서 Spring Data 프로젝트를 권장하고 있다.
- **JPA는 영속성 컨텍스트로 엔티티를 영구 저장하는 환경**이다. 트랜잭션 안에서 db안에 있는 데이터를 가져오면 영속성 컨텍스트가 유지되고, 해당 데이터의 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블의 변경을 반영한다. 즉, update 쿼리가 따로 필요없고 Entity에서 바꿔주기만 하면 끝난다. 그 데이터가 계속 유지되기 때문에. 
- `spring.jpa.hibernate.ddl-auto=none` : JPA로 테이블이 자동생성되지 않도록 한다. RDS에는 실제 운영되므로 스프링 부트에서 생성되지 않도록 해야한다.

#### h2
- 인메모리 RDB
- 의존성으로 관리 가능
- 메모리에서 실행되기 떄문에 app을 재시작할 때마다 초기화됨
- 그래서 테스트용도로 많이 쓰임
- 직접 접근하려면 웹 콘솔을 사용해야됨(`application.properties`에 따로 추가)

#### @Entity
- **테이블과 링크될 클래스**임을 나타낸다. DB와 맞닿은 클래스이므로 조심해야된다.
- 이 클래스를 기준으로 테이블이 생성되고 스키마가 변경된다.
- **그래서 Entity 클래스와 Controller에서 쓸 Dto는 분리해서 사용**한다.
- 카멜케이스, 언더스코어 네이밍으로 이름을 칭함
- SalesManager.java -> sales_manager table
- setter 메소드가 없다. 값들이 언제 어디서 변해야하는지 구분할 수 없기때문에.
	- 그래서 값을 넣을 때 생성자를 통해서 값을 채운 후, DB에 삽입하거나 그에 해당하는 메소드를 호출한다.
	- 여기서 `@Builder`를 쓰면 어느 필드에 어떤 값을 채워야할 지 명확하게 인지할 수 있다.

> Entity의 PK는 Long 타입의 auto increment를 추천. FK를 맺을 때나 인덱스에도 좋은 영향을 주지 않음

#### DAO
- DAO라고 불리는 DB Layer 접근자는 Repository라고 불린다. (ex. PostsRepository)
- `JpaRepository<Entity클래스, PK 타입>`을 상속하면 기본적인 CRUD 메소드가 자동으로 생성된다.
- `@Repository`를 추가할 필요 없으나 Entity 클래스와 같은 위치에 있어야한다. 

> 보통 규모가 있는 프로젝트에서 데이터 조회는 여러 가지 이유로 Entity 클래스 만으로 처리하기 어려워 조회용 프레임워크를 따로 사용한다고 한다.. (querydsl을 추천하심)

#### assertj 라이브러리
- 테스트 검증 라이브러리
- `assertThat, isEqualTo` 등의 메소드가 있다.
- JUnit에도 assertThat이 있지만 JUnit으로 쓰게 되면 Matcher에 있는(자동완성 되지 않는) `is`를 통해서 따로 비교해줘야 하는 등 여러모로 **assertj에 있는 assertThat**이 좋다.
- 엔티티 안에 어떤 게 있는지 확인 가능
- `JpaRepository`를 상속받아 `save, findAll` 등의 메소드를 쓸 수 있다

#### Service

- **서비스는 비즈니스로직을 처리해야 곳이 아니라 트랜잭션과 도메인 간의 순서를 보장하는 곳**
- 크게 Web(Controller, View), Service, Repository, Dto, Domain(order, billing, delivery) 레이어가 있다. 비즈니스 처리를 담당하는 곳은 Domain 레이어.
- 예를 들어, 취소 로직이 있을 때 Domain에서 취소 로직을 만들고 Service에서는 불러오기만 해서 순서를 보장한다. 

> `.map(PostsListResponseDto::new)` -> `.map(posts -> new PostsListResponseDto(post))`

#### View

템플릿 엔진 : 지정된 템플릿 양식과 데이터가 합쳐져 HTML 문서를 출력하는 소프트웨어

- ex) JSP, Vue, React 등...
- JSP같은 서버 템플릿 엔진은 서버에서 구동된다. 서버에서 문자열을 만든 뒤, HTML로 변환해 브라우저로 전달한다.
- Vue, React같은 SPA, 클라이언트 템플릿 엔진은 브라우저에서 화면을 생성한다.
- 페이지 로딩속도를 높이기 위해 CSS는 header, js는 footer에 둔다.

#### 머스테치

- 머스테치는 수 많은 언어를 지원하는 가장 **심플한 템플릿 엔진**이다.
- 스프링부트에서 공식지원하는 템플릿 엔진이기에 의존성 하나만 추가하면 버전도 신경쓰지 않아도 된다. 
- Controller에서 "index"라는 문자열을 리턴시키면 `src/main/resources/templates/index.mustache`로 전환해 View Resolver로 처리하게 됨
- `{{> }}`는 현재 머스테치 파일을 기준으로 다른 파일을 가져온다.
- index.js만의 유효범위를 만들기 위해 `var index = { ... }`를 작성한 후 `index.함수이름`으로 불러오게 하자.(index 객체 안에서만 function이 유효하도록)
- Spring Boot는 기본적으로 `src/main/resources/static`에 위치한 자바스크립트, CSS, 이미지 등 정적파일들이 URL에서 `/`로 설정한다.
- `{{# }}`는 true, false를 판단해 for문을 돌게 해주거나 실행시켜준다. true/false만 판단해주기에 최종값을 넘겨줘야 한다.
- `{{^ }}`는 해당 값이 존재하지 않을 때 보여주는 것.

#### 스프링 시큐리티

- 막강한 인증과 인가(권한부여) 기능을 가진 프레임워크
- 스프링 기반이라면 보안을 위한 표준
- 권한 코드에 `ROLE_`이 항상 앞에 있어야 한다.
- `a href="/logout"`은 스프링 시큐리티에서 기본적으로 제공하는 로그아웃 URL. 즉 개발자가 해당 컨트롤러를 만들 필요가 없다.
- `a href="/oauth2/authorization/google"`은 스프링 시큐리티에서 기본적으로 제공해주는 로그인 URL
- 기존 테스트에 시큐리티를 적용하면 인증된 사용자만 API를 호출할 수 있기 때문에 문제가 된다.
	- src/main과 src/test의 환경이 다르기 때문에 **`application.properties`를 따로 만들어줘야한다.**  test에 `application.properties`가 없다면 test는 main에 있는 `application.properties`만 가져온다. `application-oauth.properties`는 가져오지 않는다.

#### OAuth

- OAuth 구현 시 소셜 로그인 서비스로 직접 구현하지 않아도 됨.
- OAuth2 서비스는 스프링부트 1.5와 스프링부트 2.0에 차이가 있음
- `spring-security-oauth2-autoconfigure` 라이브러리를 사용할 경우, 스프링 부트 2에서도 1.5에서 쓰던 설정을 그대로 사용할 수 있음
- Spring boot 1.5를 쓰는 경우 `application.properties` 또는 `application.yml`에 url 주소를 명시해야되지만 2.0을 쓰는 경우는 enum(CommonOAuth2Provider)이 추가됨.
- `application-oauth.properties`에서 `spring.security.oauth2.client.registration.google.scope`를 별도로 등록하지 않는데 이는 기본값이 openid, profile, email이기 때문이다. 그러나 openid가 있으면 openid 서비스인 구글과 openid가 아닌 서비스(카카오, 네이버)를 나눠서 각각 서비스를 만들어야 한다. 하나의 Oauth2서비스를 위해 openid를 뺀 scope를 등록한다.
- 스프링부트에서는 **properties의 이름을 `application-xxx.properties`로 만들면 profile로 관리가 된다.** 즉, `application.properties`에 `spring.profiles.include=xxx`로 xxx를 포함할 수 있다.

**인증된 사용자 정보만 필요할 때 새로운 SessionUser 클래스를 만들어야 하는 이유**

- 직렬화를 구현해야되며 User 클래스는 엔티티이기 때문에 다른 엔티티와 관계가 형성될 수 있다.
- 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 게 유지보수에 좋다.

#### 앱을 재실행하면 로그인이 풀리는 문제는 왜 그럴까?

- 기본적으로 세션은 WAS의 메모리에 저장되고 호출된다.
- 내장 톰캣처럼 앱 실행 시 실행되는 구조에서는 실행될 때마다 항상 초기화가 된다.
- 이 외에도 또 다른 문제는 2대 이상의 서버에서 서비스한다면 톰캣마다 세션 동기화 설정을 해줘야 한다.
- 해결 방법은?
	1. 톰캣들 간의 세션 공류를 위한 추가설정
	2. DB를 세션 저장소로 사용(로그인 요청마다 DB IO 발생)
	3. Redis, Memcached와 같은 메모리 DB를 세션 저장소로 사용

#### AWS

- 같은 인스턴스를 중지하고 새로 시작할 때 항상 새로운 IP가 할당된다. 번거롭기때문에 고정 IP를 가지게 해야된다. 고정 IP를 EIP라고 한다.
- vCPU는 물리 CPU 사양의 절반정도의 사양
- t2는 요금 타입을 이야기한다.
- Mac은 그냥 가능, Windows는 putty, puttygen을 이용해야함.

아마존 리눅스 서버 생성 시 해야될 설정들

1. 프로젝트의 버전대로 **Java 8 설치**
2. 미국시간에서 한국시간으로 **타임존 변경**
3. IP만으로 서버의 역할을 구분짓기 어려우니 서버의 별명을 지어줘야 한다.  **호스트네임 변경**

- AWS에서는 모니터링, 알람, 백업, HA 구성 등을 지원하는 RDS 제공한다.
- utf8과 utf8mb4의 차이는 utf8mb4는 이모지 저장이 가능하다.

### 쉘 스크립트
```shell
echo "> 현재 구동중인 애플리케이션 pid 확인"

# pgrep을 통해 프로젝트 이름의 pid를 찾는다.
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}*.jar)

echo "현재 구동중인 애플리케이션 pid : $CURRENT_PID"

# 구동중인 애플리케이션이 없다면 echo만 해주고 있다면 종료해준다.
if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi
```
> 예전에 배포했을 때는 매번 그 pid를 찾아서 kill해주고 다시 nohup으로 실행하고 그랬었는데 쉘 스크립트로 하면 이렇게 편하다.

배포하고 나서 Google 로그인 연동과 네이버 로그인 연동도 AWS DNS 주소에 따라 다시 설정해줘야 한다.

### Travis CI 배포 자동화

**CI(Continuous Integration)**

- git에 push가 되면 **자동으로 테스트와 빌드가 수행**되어 안정적인 배포 파일을 만드는 과정

**CD(Continuous Deployment)**

- CI에 했던 빌드 결과를 **자동으로 운영서버에 무중단 배포까지 진행**되는 과정

하나의 프로젝트를 여러 개발자가 함께 개발을 진행할 때 자동으로 코드를 병합해 생산성을 높이기 위해서 CI/CD가 생겨났으며 현재 이 프로젝트가 완전한 상태임을 보장하기 위해 **테스팅 자동화**가 특히나 중요하다.

#### Travis CI

- 깃허브에서 제공하는 무료 CI 서비스
- `.travis.yml`을 통해 S3로 배포 파일을 올릴 수 있도록 함

#### AWS S3

- 일종의 파일서버.
- 이미지 파일을 비롯한 정적 파일 관리, 배포 파일 관리
- 실제 배포는 AWS CodeDeploy를 사용하지만 CodeDeploy는 저장기능이 없어 Jar파일을 S3에 먼저 저장하고 이걸 CodeDeploy가 가져갈 수 있도록 한다.

#### AWS CodeDeploy

- AWS의 배포서비스

#### AWS IAM(Identity and Access Management)

- AWS 서비스에 관련된 접근방식과 권한 관리
- 사용자 : AWS 서비스 외에 사용할 수 있는 권한(여기서 만든 엑세스 키를 가지고 Travis CI에서 사용할 수 있음)
- 역할 : AWS 서비스에만 할당할 수 있는 권한

#### 배포 관련

- 규모가 있는 서비스의 경우, Test와 Build에 많은 시간이 걸린다. 성공적인 배포파일일 경우, 시간을 줄이고자 빌드없이 배포만 할 때가 있어 **빌드와 배포는 따로하는 게 좋다.**
- 현재  코드저장소의 역할은 github, 빌드하고 테스트하는 역할은 Travis CI, 배포서비스는 AWS Code Deploy가 한다.
- 즉, github에 올려진 코드를 가지고 Travis CI를 통해 Test와 Build를 거쳐 jar 파일을 만들고 이 jar 파일을 AWS S3에 올린다. 그리고 나서 AWS CodeDeploy가 jar 파일을 받아 AWS EC2에 배포한다.
- `/opt/codedeploy-agent/deployment-root/deployment-logs/codedeploy-agent-deployments.log`은 CodeDeploy의 로그파일

### 24시간 365일 중단 없는 서비스를 만들자

배포 자동화 환경을 구축했지만 배포하는 동안 새로운 Jar가 실행되기 전까지 기존 Jar를 종료시키기 때문에 서비스가 중단된다. 24시간 언제라도 중단되지 않는 서비스를 구축해보자.

**NGINX**

- 웹 서버, 리버스 프록시, 캐싱, 로드 밸런싱, 미디어 스트리밍 등을 위한 오픈소스 소프트웨어

리버스 프록시
- 외부의 요청을 받아 백엔드 서버로 요청을 전달하는 행위
- **NGINX 서버는 요청을 전달하고 실제 요청에 대한 처리는 웹 서버가 처리한다.**

하나의 EC2 혹은 리눅스 서버에 NGINX 1대와 스프링 부트 Jar 2대를 사용한다.
- NGINX는 80, 443 포트를 사용하고 스프링부트는 8081과 8082포트로 실행한다.
- 즉 사용자는 80, 443 포트로 들어오면 되고 NGINX가 완전히 배포된 포트(8081 또는 8082)로 이동하게끔 해주면 된다.

## 개발 후 프로젝트의 전체적인 Flow

### View(사용자 입장)

1. 사용자가 웹 사이트로 들어온다.
2. EC2 서버에 있는 `nginx.conf`파일을 읽어 기본 listen port인 80포트로 들어가게 된다.
3. `location / {}`이므로 `/`인 최상위 루트로 들어왔을 때 `proxy_pass`에 정해둔 `$service_url`변수에 지정된 포트로 가게 된다.
4. `index.mustache`가 로딩되고 js파일도 로딩된다.
5. `index.js`에 정의해둔 on click 메소드에 따라 POST, PUT, DELETE와 같은 HTTP 메서드 형식으로 요청을 보낸다.
6. OAuth2 같은 경우 해당 요구에 맞는 형식으로 보내진다.
7. `/`로 오므로 `@LoginUser`를 통해서 SessionUser를 검사하게 된다.

### AWS EC2 서버 및 Domain, Database

1. `@SpringBootApplication`부터 Bean을 읽어가며 시작한다.
2. `@RequiredArgsConstructor`를 통해 bean이 주입된다.
3. `@RequestBody`를 통해 내가 만든 Dto(객체 형태)로 받을 수 있다.
4. JpaRepository를 상속받아 CRUD가 기본적으로 만들어지고 update도 엔티티에서 만들면 된다.
5. JpaAuditing을 통해 등록시간과 수정시간을 자동으로 저장한다.

### Travis CI 및 AWS S3, AWS CodeDeploy, Nginx 무중단 배포

1. 내가 git으로 commit하고 push한다.
2. github에 코드들이 저장되고 연동된 Travis CI가 `.travis.yml`에 지정한대로 실행된다.
    1. zip파일로 만든다.
    2. S3 버킷에 전송한다.
    3. Travis CI가 끝나면 메일이 오고 자동으로 CodeDeploy가 실행된다.
3. AWS S3에 올려진 Zip 파일을 가지고 CodeDeploy가 `appspec.yml`에 정해진 대로 배포를 시작한다.
    1. `stop.sh`부터 시작인데 `profile.sh`에 있는 `find_idle_profile()`을 통해 get방식으로 `/profile`경로로 요청을 보낸다.
    2. `/profile`로 요청보내면 실행중인 Profile(`application-xxx.properties`로 관리되고 있는) String(`xxx`)을 반환한다.
    3. 쉬고있는 profile을 `IDLE_PROFILE`이라는 변수에 담아 쉬고 있는 게 `real1`이면 `8081` 포트를, `real2`면 `8082` 포트를 반환한다.
    4. `stop.sh`를 통해 쉬고있는데 실행중이던 profile(스프링부트)을 kill한다.
    5. `start.sh`를 통해 쉬고있는 profile을 실행시킨다.
    6. `health.sh`를 통해 쉬고있는`/profile` 포트로 get을 요청해 `RESPONSE` 값을 받아오고 여기에 `real`문자열이 있는지 확인한다.
    7. `real` 문자열이 있으면 `switch_proxy`를 통해  service_url을 바꿔주고 nginx를 리로드한다.

## 1인 개발 시 도움이 될 도구와 조언들

댓글 서비스
- Disqus
- LiveRe
- Utterance(개발자용)

SNS 자동 포스팅 연동 서비스
- Zapier
- IFTTT

방문자 분석
- 구글 애널리틱스

CDN
- 클라우드플레어

이메일 마케팅
- Mailchimp

1인 개발 팁
- 쉬운 주제부터 완성해나가기
- 확실한 리더가 있는 게 아니라면 혼자서 개발하기
- 가장 자신있는 개발 환경 사용

## 단축키
Ctrl + Shift + k : git push창
Ctrl + k : git commit 창
Ctrl + Shift + a : Action 창
Ctrl + alt + l : 자동정렬
Ctrl + Shift + z : Ctrl + z의 반대
Ctrl + Shift + N : 파일 검색

## 출처
- [스프링 부트와 AWS로 혼자 구현하는 웹 서비스 책](https://jojoldu.tistory.com/463)