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
CREATE TABLE user (
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
CREATE TABLE board (
                      id int AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(100) NOT NULL UNIQUE,
                      content VARCHAR(4000) NOT NULL,
                      author_id int NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 3) 첨부파일 테이블 생성
```sql
CREATE TABLE attach_file (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             file_path VARCHAR(255) NOT NULL COMMENT '파일의 경로',
                             file_key VARCHAR(255) NOT NULL COMMENT '파일을 식별하는 키',
                             file_dir VARCHAR(255) NOT NULL COMMENT '파일이 저장된 디렉터리',
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일시',
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '업데이트 일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='첨부 파일 테이블';
```

### 4) video
```sql
CREATE TABLE video (
   video_id VARCHAR(36) PRIMARY KEY, -- UUID를 저장하기 위한 VARCHAR(36) 타입
   video_title VARCHAR(255) NOT NULL, -- 제목을 저장할 VARCHAR 타입, 길이는 255로 설정
    owner_id INT NOT NULL
);
```

```sql
CREATE TABLE edited_frame (
  id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유 ID (자동 증가)
  video_id VARCHAR(36) NOT NULL,        -- 관련 비디오의 UUID
  sequence INT NOT NULL,                -- 프레임 시퀀스 번호
  edit_id BIGINT NOT NULL,              -- 편집 작업 ID
  edit_file_id BIGINT NOT NULL,         -- 편집된 프레임의 파일 ID (외부 저장소의 파일 참조)
  FOREIGN KEY (video_id) REFERENCES video(video_id) -- video 테이블과 관계 설정
);

```

```sql
CREATE TABLE original_frame (
    video_id VARCHAR(36) NOT NULL,        -- 관련 비디오의 UUID
    sequence INT NOT NULL,                -- 프레임 시퀀스 번호
    frame_file_id BIGINT NOT NULL,        -- 원본 프레임 파일 ID (외부 저장소의 파일 참조)
    PRIMARY KEY (video_id, sequence),     -- 비디오 ID와 시퀀스를 복합 키로 설정
    FOREIGN KEY (video_id) REFERENCES video(video_id) -- video 테이블과 관계 설정
);

```

```sql
CREATE TABLE video_edit (
    id INT AUTO_INCREMENT PRIMARY KEY,   -- 고유 ID (자동 증가)
    video_id VARCHAR(36) NOT NULL,       -- 관련 비디오의 UUID
    edit_title VARCHAR(255) NOT NULL,    -- 편집 작업 제목
    FOREIGN KEY (video_id) REFERENCES video(video_id) -- video 테이블과 관계 설정
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
또는
```shell
docker-compose build spring-app
``
