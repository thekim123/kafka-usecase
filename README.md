# 개발환경 세팅

## 1. 디비 설치
### 1) MariaDB 도커 설치
- 이미지 다운로드
```shell
docker pull mariadb:latest
```
- 컨테이너 실행
```shell
docker run -d --name mariadb-container -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=namu-jwt-test -e MYSQL_USER=namu-jwt-test -e MYSQL_PASSWORD=1234 -p 3321:3306 --restart unless-stopped mariadb:latest
```

### 2) Redis 도커 설치 및 컨테이너 실행
- 이미지 다운로드
```shell
docker pull redis:latest
```
- 컨테이너 실행

볼륨이 있는 명령어
```shell
docker run -d --name redis-container -p 6379:6379 -v redis-data:/data --restart unless-stopped redis:latest --requirepass 1234
```

볼륨이 없는 명령어
```shell
docker run -d --name redis-container -p 6379:6379 --restart unless-stopped redis:latest --requirepass 1234
```



## 2. 테이블 생성
### 1) 유저 테이블 생성
```sql
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 2) 게시물 테이블 생성
```sql
CREATE TABLE Board (
                      id int AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(100) NOT NULL UNIQUE,
                      content VARCHAR(4000) NOT NULL,
                      author_id int NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```


## 3. redis 디버깅
- RBook 설치
```shell
npx rbook
```

- `RefreshTokenIndex` 조회
```redis
HGETALL refreshTokenIndex:<username>
```

- `RefreshTokenIndex`의 유효시간 조회
```redis
TTL refreshTokenIndex:<username>
```
- `RefreshTokenIndex` 삭제
```redis
DEL refreshTokenIndex:<username>
```

- `RefreshToken` 조회
```redis

```


## 4. Commit Message Format
```
$ git commit -m "feat: 로그인 기능 추가"
```

```
feat: 회원가입 기능 추가
fix: 게시판 수정시 전달하는 파라미터 값 수정
design: css 변경
breaking change: API 변경
hotfix: npe 오류 수정
style: 코드 포맷 변경
refactor: 변수명 변경
comment: 주석 추가
docs: 문서 수정
test: 테스트 추가
rename: 파일명 수정, 경로 이동
remove: 삭제 
```


## 5. docker-compose 
```
docker-compose up --build -d
```

일반 *``up -d``*옵션 사용하면 새롭게 빌드된 jar 파일을 사용하지 않고, 

기존 jar로 dockerfile이 만든 캐시된 이미지를 사용하기 때문에 변경사항이 제대로 반영되지 않을 수 있음.

매번 실행 시 강제로 이미지를 빌드하려면 *``--build``* 플래그를 추가하면 됨
