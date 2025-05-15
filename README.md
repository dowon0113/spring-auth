# **Spring Boot 기반 JWT 인증/인가**

- **Spring Boot**를 이용하여 JWT 인증/인가 로직과 API를 구현합니다.
- **Junit** 기반의 테스트 코드를 작성합니다.
- **Swagger** 로 API를 문서화합니다.

## 프로젝트 실행 방법

이 프로젝트는 별도의 데이터베이스나 외부 설정 없이 **메모리 기반**으로 동작합니다.  
따라서 아래 명령어만으로 바로 실행 및 테스트가 가능합니다:

### Gradle로 실행

```bash
./gradlew bootRun
```

## 기능 개발

<aside>

 **1. 목표**

- **사용자 인증 시스템**을 구축합니다. (회원가입, 로그인)
- **JWT(Json Web Token) 기반 인증 메커니즘**을 구현하여 보안성을 강화합니다.
- **역할(Role) 기반 접근 제어**를 적용하여 관리자(Admin) 권한이 필요한 API를 보호합니다.
</aside>

<aside>

**2. 구현 포인트**

- [ ]  유저(User) 및 관리자(Admin) 회원가입, 로그인 API를 개발합니다.
- [ ]  JWT를 이용하여 Access Token을 발급하고 검증하는 로직을 적용합니다.
- [ ]  일반 사용자(User)와 관리자(Admin) 역할(Role)을 구분하여 특정 API 접근을 제한합니다.
</aside>

**3. API**
<details>
<summary>회원가입</summary>
  
  - **HTTP Method:** POST
  - **URL**: `0.0.0.0:8080/v1/auth/signup`
  - **Request Body**
    ```json
    {
      "username": "JIN HO",
      "password": "12341234",
      "nickname": "Mentos"
    }
    ```
  - **Response Body**
    - 성공
    ```json
      {
        "username": "JIN HO",
        "nickname": "Mentos",
        "roles": [
          {
            "role": "USER"
          }
        ]
      }
    ```
    - 실패(중복가입)
    ```json
      {
        "error": {
          "code": "USER_ALREADY_EXISTS",
          "message": "이미 가입된 사용자입니다."
        }
      }
    ```

</details>
<details>
<summary>로그인</summary>

  - **HTTP Method:** POST
  - **URL**: `0.0.0.0:8080/v1/auth/login`
  - **Request Body**
    ```json
      {
        "username": "JIN HO",
        "password": "12341234"
      }
    ```
  - **Response Body**
    - 성공
    ```json
      {
        "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"
      }
    ```
    - 실패(잘못된 계정 정보)
    ```json
      {
        "error": {
          "code": "INVALID_CREDENTIALS",
          "message": "아이디 또는 비밀번호가 올바르지 않습니다."
        }
      }
    ```
</details>
<details>
<summary>관리자 권한 부여</summary>

  - **HTTP Method:** PATCH
  - **URL**: `0.0.0.0:8080/v1/users/{userId}/roles`
  - **Path Variable 예시**
    ```
      v1/users/8865620366019808792/roles
    ```
  - **Response Body**
    - 성공
    ```json
      {
        "username": "JIN HO",
        "nickname": "Mentos",
        "roles": [
          {
            "role": "Admin"
          }
        ]
      }
    ```
    - 실패(권한 부적)
    ```json
      {
        "error": {
          "code": "ACCESS_DENIED",
          "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
        }
      }
    ```
</details>

**4. JWT를 이용한 Access Token 발급 및 검증**
<details>
<summary>토큰 발급</summary>

  - 토큰은 비밀 키로 서명되었으며, 만료 시간은 1시간입니다.
  - 토큰 구조
    - **Header**: 알고리즘 및 토큰 유형 정보
    - **Payload**: 사용자 ID(sub), 역할(roles), 발급 시간(iat), 만료 시간(exp) 등의 정보
    - **Signature**: 헤더와 페이로드가 변조되지 않았음을 확인하는 서명
</details>
<details>
<summary>토큰 검증</summary>

  - 모든 보호된 API 요청에는 HTTP 요청 헤더에 토큰을 포함됩니다.
  - 헤더 형식: `Authorization: Bearer [토큰]`
  - 서버는 아래의 검증을 수행합니다.
    - 토큰 존재 여부 확인
    - 서명 검증 (서버의 비밀 키로 서명 확인)
    - 토큰 만료 여부 확인
    - 토큰에 포함된 권한 정보 추출
    - SecurityContext에 인증 정보 설정
    - 요청한 API에 접근 권한이 있는지 확인
</details>
<details>
<summary>에러처리</summary>

  - 토큰이 유효하지 않거나 만료된 경우, 다음 응답을 반환합니다.
    ```
    {
      "error": {
        "code": "INVALID_TOKEN",
        "message": "유효하지 않은 인증 토큰입니다."
      }
    }
    ```
- 권한이 부족한 경우, 다음 응답을 반환합니다.
    ```
    {
      "error": {
        "code": "ACCESS_DENIED",
        "message": "접근 권한이 없습니다."
      }
    }
    ```  

</details>

## 테스트
- 회원가입
    - 정상적인 사용자 정보와 이미 가입된 사용자 정보에 대해 테스트합니다.
- 로그인
    - 올바른 자격 증명과 잘못된 자격 증명을 테스트하여 성공/실패 시의 응답 구조가 예상과 동일한지 확인합니다.
- 관리자 권한 부여
    - 관리자 권한을 가진 사용자가 요청할 때 정상 처리되는지 테스트합니다.
    - 일반 사용자가 요청할 때 권한 오류가 발생하는지 테스트합니다.
    - 존재하지 않는 사용자에게 권한을 부여하려 할 때 적절한 오류 응답이 반환되는지 테스트합니다.

## API 명세서 - Swagger 연동
**목표**

- 각 엔드포인트, 요청/응답 구조, 상태 코드 등을 한눈에 파악할 수 있도록 문서화합니다.
- API RestDocs 문서화 테스트 코드 작성 및 Swagger 연동
  - 회원가입 성공/실패 케이스 테스트 및 문서화
  - 로그인 성공/실패 케이스 테스트 및 문서화
  - 관리자권한 부여 성공/실패 케이스 테스트 및 문서화

**실행방법**
```
http://localhost:8080/swagger-ui/index.html
```
로컬 서버 실행 후 별도 빌드 없이 실행가능합니다. swagger-ui에서 명세서를 확인할 수 있습니다.





