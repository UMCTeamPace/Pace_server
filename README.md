# Pace Server 🏃‍♂️

> **UMC 9기 'Pace' 서버팀** 

## 📖 프로젝트 개요

**Pace**는 사용자의 일정과 이동 경로를 효율적으로 관리할 수 있도록 돕는 모바일 애플리케이션입니다. **카카오 로그인**을 통한 간편한 인증을 지원하며, 대중교통 데이터를 활용한 일정 관리, 경로 추천, 장소 저장 등의 기능을 제공합니다.

## 🛠 백엔드 기술 스택 (Tech Stack)

### 환경 (Environment)
*   ![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
*   ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F?style=flat&logo=springboot&logoColor=white)
*   ![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=flat&logo=gradle&logoColor=white)

### 데이터베이스 & ORM
*   ![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)
*   **Spring Data JPA**
*   **QueryDSL 5.0**

### 보안 & 인증 (Security & Auth)
*   **Spring Security**
*   **OAuth 2.0 (Kakao Login)**
*   **JWT (JSON Web Token)**

### API & 문서화
*   **SpringDoc OpenAPI (Swagger UI)**
*   **WebFlux (WebClient)** - 외부 API 연동

### 유틸리티 (Utilities)
*   **Apache POI** - 엑셀 데이터 처리 (버스/지하철 정보)
*   **Lombok**

## 📂 프로젝트 구조

```bash
src/main/java/com/example/pace
├── PaceApplication.java
├── domain
│   ├── auth          # 인증 (Kakao, JWT)
│   ├── member        # 회원 관리, 장소 보관함, 설정
│   ├── schedule      # 일정 및 경로 관리
│   ├── terms         # 약관 관리
│   └── transit       # 대중교통 데이터 로직
└── global
    ├── apiPayload    # 표준 API 응답 구조
    ├── auth          # 시큐리티 설정, Custom User Details
    ├── config        # 앱 설정 (Swagger, WebClient 등)
    └── entity        # Base Entities
```

## ✨ 주요 기능

### 1. 인증 (Authentication)
*   **카카오 로그인**: Kakao OAuth2를 이용한 간편 로그인.
*   **토큰 관리**: Access/Refresh Token 발급 및 재발급(Reissue).
*   **계정 관리**: 로그아웃 및 회원 탈퇴 기능.

### 2. 일정 관리 (Schedule Management)
*   **생성/삭제**: 개인 일정 등록 및 삭제.
*   **조회**: 일별, 월별 일정 목록 조회.
*   **경로**: 대중교통 정보를 활용한 일정 경로 관리.

### 3. 회원 및 설정 (Member & Settings)
*   **프로필**: 사용자 정보 관리.
*   **장소 보관함**: 자주 가는 장소 즐겨찾기 및 그룹 관리.
*   **온보딩**: 초기 사용자 설정 프로세스.

### 4. 대중교통 통합 (Transit Integration)
*   **데이터 로딩**: 버스 및 지하철 데이터(Excel/JSON)를 로드하여 경로 계산에 활용.
