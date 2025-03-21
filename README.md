# Tourist attraction reservation service
관광지 예약 및 리뷰, 문의 사이트


## 주제

지역 기반 관광지 예약 및 리뷰 사이트는 사용자가 다양한 관광 명소를 예약하는 것을 돕는 시스템 입니다. 일반 회원 는 관광지 리스트를 보면서 원하는 관광지를 쉽게 검색하거나 예약할 수 있으며, 관광지 관리자도 예약자 명단을 보거나, 관광지 관리를 할 수 있습니다. 추가적으로 서비스 회원들은 게시글을 통해 리뷰나 문의를 남길 수 있으며, 댓글을 통해 서로 소통이 가능합니다.


## 개발 환경

 - **programming Language**: `JAVA 21`
 - `Spring Boot 3.4.3`
 - `JPA`, `Lombok`, `Spring Security`
 - **DataBase**: `MySQL`
 - **Front End**: `Thymeleaf`, `JavaScript`
 - **IDE** : `IntelliJ IDEA` , `MySQL Workbench`
 - `GitHub`, `Docker`, `Docker compose`

   
## 역할



## 기능


#### 회원 기능

#### 관광지 관리 및 예약 기능

#### 게시글 및 댓글 기능

- 게시글 쓰기, 읽기, 수정, 삭제 기능 
  - 게시글 종류에는 `일반`, `리뷰`, `문의`, `신고`, `공지` 가 있다.
  - 게시글은 종류별로 모아서 볼 수 있으며, 페이지 기능과 다양한 옵션이 포함된 검색 기능이 있다.
  - 게시글 종류에 따라 이미지를 포함하거나, 평점, 관광지를 선택할 수 있다.
  - 사용자의 역할에는 `일반 유저`, `관광지 관리자`,`관리자` 가 있으며, 사용자의 역할별로 읽기, 쓰기, 수정, 삭제를 제한하였다.

## Architecture 


## DB Diagram


## API


## 도커 환경에서 구동 방법(윈도우 기준)

 1. Docker와 Docker compose를 설치한다.
 2. docker-compose-extra-2.yml 파일을 다운로드 받는다.
 3. mysql-init/init.sql 파일을 다운로드 받아서 ./mysql-init 에 위치 시킨다.
 4. cmd 창에서 docker-compose-extra-2.yml 파일이 있는 위치로 이동하고 'docker compose -f docker-compose-extra-2.yml up -d' 명령을 실행한다.
 5. 'localhost:8080'(기본값)으로 접속하면 서비스를 운영할 수 있다.
  - docker-compose-extra-2.yml 파일, init.sql 파일 변경으로 다양한 설정값을 적용할 수 있다.
  - 초기 데이터는 github의 src/main/resources/data/insert_data.sql 파일을 사용하거나, ./sql 폴더에 직접 insert_data.sql문을 작성해도 된다.
    
