# Service Convention

## 목적

Service와 Component는 유스케이스 흐름, 비즈니스 규칙, 트랜잭션, 협력 객체 호출을 담당한다. HTTP 응답 조립과 단순 객체 매핑은 담당하지 않는다.

## 현재 구조와 명명

조회·변경 책임을 분리할 수 있는 도메인은 다음 구조를 사용한다.

```text
{domain}/service/
├── command/{Domain}CommandService.java
└── query/{Domain}QueryService.java
```

현재 프로젝트에는 예외가 있다.

- `AuthCommandService`는 인증 변경 흐름을 담당하고 `KakaoApiQueryService`가 카카오 조회를 담당한다.
- `ScheduleCommandService`는 생성·수정·삭제뿐 아니라 일정 조회도 제공한다.
- `BusNetworkService`와 `SubwayNetworkService`는 `@Component`로 등록된 네트워크·정적 데이터 서비스다.
- `RouteCommandService`는 이름은 Command지만 경로 검색을 제공하고 Google client와 transit service를 조합한다.

새 기능에서는 조회와 변경을 가능한 범위에서 분리하되, 기존 서비스의 메서드를 단순 규칙 준수를 위해 이동하지 않는다.

## 책임

Service 또는 Component가 담당하는 항목:

- 유스케이스 실행 순서
- 여러 Repository·Converter·외부 Client·Service의 조합
- DB 조회와 저장
- 권한·존재 여부·상태 같은 비즈니스 검증
- 도메인 예외 발생
- transaction 경계 설정

담당하지 않는 항목:

- `HttpServletRequest`, `ResponseEntity`, Controller 어노테이션
- Swagger 문서화
- 요청 validation annotation 정의
- 공통 HTTP 응답 포장
- Converter가 처리할 단순 객체 매핑의 반복 구현

## 트랜잭션

- DB 변경 메서드는 필요한 범위에 `@Transactional`을 둔다.
- 읽기 전용 DB 조회는 `@Transactional(readOnly = true)`를 우선한다.
- 현재 프로젝트는 주로 메서드 수준 transaction을 사용한다.
- 외부 API 호출을 불필요하게 긴 DB transaction 안에 포함하지 않는다.
- `@Transactional`을 붙인 같은 클래스 내부 호출로 프록시 전파를 기대하지 않는다.
- 새로운 서비스는 클래스 전체에 일괄 `@Transactional`을 붙이기보다 메서드 책임에 맞춰 선언한다.

## 의존성

- Repository, 외부 API client, loader는 생성자 주입을 사용한다.
- Converter는 현재 static 유틸리티 방식이므로 주입하지 않는다.
- Service 사이의 연쇄 호출은 유스케이스에 필요한 최소 범위로 제한한다.
- 새로운 Service 인터페이스는 여러 구현체 교체나 테스트 경계가 실제로 필요한 경우에만 만든다.
- 순환 의존을 만들지 않는다.

## 반환과 예외

- Controller에 Entity를 직접 반환하지 않는다.
- 응답 DTO 조립은 Converter 또는 해당 도메인의 명확한 조립 계층에 둔다.
- 조회 실패와 규칙 위반은 해당 도메인의 ErrorCode를 사용한다.
- 새 비즈니스 오류에 `IllegalArgumentException`을 임의로 사용하지 않는다.
- 전역 예외 처리와 공통 응답 변환은 `GeneralExceptionAdvice`에 위임한다.

## 외부 API 연동

- 외부 요청 DTO와 원본 응답 DTO는 외부 계약을 표현하는 타입으로 분리한다.
- API 키와 endpoint는 properties/config 또는 환경 변수로 관리한다.
- 외부 실패를 도메인에서 의미 있는 ErrorCode로 변환한다.
- 외부 원본 필드명을 앱 응답 DTO에 무분별하게 노출하지 않는다.
- 재시도, timeout, fallback이 요구사항이 아니라면 임의로 추가하지 않는다.

## 금지 사항

- Controller에서 Repository를 직접 호출하도록 Service를 우회하는 것
- Service에서 HTTP 응답 객체를 생성하는 것
- Converter를 Bean으로 만들어 무분별하게 주입하는 것
- 비밀값·토큰을 로그로 출력하는 것
- 외부 API 원본 응답을 그대로 성공 응답으로 반환하는 것
- 구현되지 않은 CQRS·Facade·추상화를 형식적으로 추가하는 것
