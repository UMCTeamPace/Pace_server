# Architecture & Responsibility Convention

## 목적과 기준

이 문서는 현재 Pace 백엔드의 구조와 계층 책임을 설명한다. 코드에 아직 없는 기능을 현재 구조로 기록하지 않으며, 새 기능 규칙과 현재 레거시 상태를 구분한다.

세부 규칙은 다음 문서를 따른다.

- [`controller_convention.md`](./controller_convention.md)
- [`service_convention.md`](./service_convention.md)
- [`dto_convention.md`](./dto_convention.md)
- [`converter_convention.md`](./converter_convention.md)
- [`exception_convention.md`](./exception_convention.md)
- [`testing_convention.md`](./testing_convention.md)

## 현재 애플리케이션 구조

실제 애플리케이션 소스 루트는 `com.example.pace`다.

```text
src/main/java/com/example/pace
├── PaceApplication.java
├── domain
│   ├── auth        카카오 로그인과 JWT 재발급
│   ├── member      회원, 온보딩, 설정, 장소 그룹·저장 장소
│   ├── schedule    일정, 경로, Google Directions 연동
│   └── transit     버스·지하철 데이터 로딩, 경로 보조, 실시간 도착
└── global
    ├── apiPayload  공통 응답, 성공·실패 코드, 전역 예외 처리
    ├── auth        Spring Security, JWT filter, 인증 사용자
    ├── config      JWT, Redis, WebClient, Swagger 등 설정
    ├── controller  health endpoint
    ├── entity      BaseEntity
    └── util        JWT·Redis utility
```

현재 도메인별 주요 패키지는 `controller`, `controller/docs`, `dto/request`, `dto/response`, `entity`, `repository`, `service`, `converter`, `exception`을 사용한다. 모든 도메인이 모든 패키지를 갖는 것은 아니다.

## 책임과 의존 방향

```text
Controller
  → Service 또는 Component
    → Repository / Converter / 외부 API Client / Loader
      → Entity 또는 외부 시스템
```

- Controller는 HTTP 입력과 공통 응답 포장을 담당한다.
- Service 또는 Component는 유스케이스 흐름, 비즈니스 규칙, 트랜잭션을 담당한다.
- Repository는 DB 조회·저장을 담당한다.
- Converter는 DTO·Entity·외부 응답 사이의 순수한 객체 변환을 담당한다.
- Infrastructure client는 외부 API 요청과 원본 응답 수신을 담당한다.
- Loader는 `src/main/resources/data`의 버스·지하철 자료를 읽고 애플리케이션에서 사용할 조회 구조를 제공한다.
- Entity는 persistence 상태와 도메인 상태 변경을 담당한다.
- `global`은 공통 인프라와 횡단 관심사를 제공하며 특정 도메인의 업무 규칙을 소유하지 않는다.

새 코드는 Controller → Service/Component → Repository·외부 Client·Converter 방향을 유지한다. Controller에서 Repository를 직접 호출하거나 Converter에서 DB·외부 API를 호출하지 않는다.

## 도메인 책임과 주요 흐름

| 도메인 | 현재 책임 | 주요 흐름 |
| --- | --- | --- |
| `auth` | 카카오 로그인, access/refresh/temp token 발급과 재발급 | `AuthController` → `AuthCommandService` → `KakaoApiQueryService`·회원 조회/생성·`JwtUtil`·`RedisUtil` |
| `member` | 회원 탈퇴·로그아웃, 온보딩, 설정, 장소 그룹·저장 장소 | 각 Controller → Command/Query Service → Repository·Converter |
| `schedule` | 일정 CRUD, 일정 경로 저장·삭제·수정, Google Directions 경로 검색 | `ScheduleController`/`ScheduleRouteController`/`RouteController` → schedule service → repository·Google client·transit service |
| `transit` | 버스·지하철 자료 로딩, 정류장 경로 계산, 서울 실시간 지하철 도착, 버스 출발 정류장 식별 | `TransitController` → `SubwayApiQueryService`·`BusNetworkService`; schedule route가 network service를 협력자로 사용 |
| `global` | `ApiResponse`, 성공·실패 코드, `GeneralExceptionAdvice`, JWT·Redis·WebClient 설정 | 모든 도메인의 공통 경계 |

### 인증된 요청 흐름

`JwtExceptionFilter`와 `JwtAuthFilter`가 토큰을 처리하고 `SecurityContext`를 구성한다. Controller는 `@AuthenticationPrincipal CustomUserDetails`로 현재 회원을 받는다. 인증 실패 응답은 `AuthenticationEntryPointImpl` 또는 JWT filter 경계에서 처리한다.

### 외부 연동

- Kakao: `domain/auth/service/query/KakaoApiQueryService`, `KaKaoProperties`
- Google Directions: `domain/schedule/infrastructure/GoogleDirectionApiClient`, `GoogleDirectionApiConfig`
- 서울 지하철 실시간 API: `domain/transit/service/query/SubwayApiQueryService`, `SubwayProperties`, `WebClientConfig`
- 버스·지하철 정적 데이터: `domain/transit/loader`, `src/main/resources/data`

외부 API 키와 DB·Redis 설정은 `application.yaml`의 환경 변수 바인딩을 사용한다. 비밀값을 코드, DTO, 로그, 테스트 fixture에 하드코딩하지 않는다.

## 공통 API 경계

- 성공 응답의 기본 타입은 `ApiResponse<T>`다.
- `ApiResponse`는 `isSuccess`, `code`, `message`, `result`를 제공한다.
- 성공 코드는 `BaseSuccessCode`, 실패 코드는 `BaseErrorCode`를 구현한다.
- `GeneralExceptionAdvice`는 `GeneralException`, validation `BindException`, 그 외 예외를 `ApiResponse`로 변환한다.
- 예외 advice의 `ResponseEntity`는 HTTP 상태를 설정하기 위한 전역 예외 경계이며, 일반적인 성공 응답의 표준으로 해석하지 않는다.

## 현재 코드에서 유지해야 할 사실

- `domain/schedule`의 일부 Controller와 `SavedPlaceController`는 아직 `ResponseEntity<ApiResponse<...>>`를 반환한다.
- `RouteController`는 현재 `ResponseEntity<RouteListResDTO>`를 반환하며 공통 `ApiResponse`를 사용하지 않는다.
- `HealthCheckController`는 `/health`에서 문자열 `OK`를 반환한다.
- `ScheduleCommandService`는 이름과 달리 일정 조회도 제공한다. 새 기능에서는 Query/Command 책임을 분리하되, 이 클래스의 기존 메서드를 무단으로 이동하지 않는다.
- `BusNetworkService`와 `SubwayNetworkService`는 `@Service`가 아니라 `@Component`로 등록되어 있다.
- 모든 Controller가 Docs 인터페이스를 구현하는 것은 아니다. 새 API는 가능하면 `{Domain}ControllerDocs`를 함께 두되, 기존 API의 일괄 리팩터링을 이 규칙의 필수 범위로 보지 않는다.
- `ScheduleException`은 현재 `GeneralException`을 상속하지 않는 별도 예외다. 새 도메인 비즈니스 예외는 `GeneralException` 기반으로 작성한다.

## 새 기능 추가 확인 순서

- [ ] `com.example.pace.domain`의 적절한 도메인과 계층을 선택했는가?
- [ ] 기존 Service·Repository·외부 Client·Loader를 재사용할 수 있는가?
- [ ] 요청·응답 DTO와 공통 `ApiResponse` 계약을 확인했는가?
- [ ] 외부 API 키·DB·Redis 비밀값이 코드와 로그에 노출되지 않는가?
- [ ] 도메인 예외와 ErrorCode를 기존 전역 advice에 연결했는가?
- [ ] 관련 unit test 또는 필요한 integration test 경계를 선택했는가?
- [ ] `git diff --check`와 관련 focused test를 실행했는가?
