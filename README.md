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

```sql
CREATE TABLE Board (
                      id int AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(100) NOT NULL UNIQUE,
                      content VARCHAR(4000) NOT NULL,
                      authorId int NOT NULL,
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



