# Testing Convention

## 범위

현재 프로젝트는 JUnit 5, Spring Boot Test, AssertJ, Mockito를 사용한다. 새 테스트에 적용하되, 기존 테스트를 형식만 맞추기 위해 일괄 수정하지 않는다.

## 테스트 경계

### 순수 Service/Component 단위 테스트

- 생성자 주입 대상만 Mockito로 격리할 수 있으면 Spring context를 올리지 않는다.
- `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`를 사용한다.
- Repository·외부 Client처럼 테스트 대상의 경계에 있는 의존성만 mock한다.
- Converter, 정적 데이터 처리, 순수 로직은 실제 객체를 우선 사용한다.
- 반환값, 도메인 예외, 중요한 collaborator 호출을 검증한다.

현재 예시: `BusNetworkServiceUnitTest`, `RouteCommandServiceUnitTest`.

### SpringBootTest와 외부 인프라 테스트

- 여러 Spring Bean의 wiring이나 실제 DB·외부 API가 필요한 경우에만 `@SpringBootTest`를 사용한다.
- 현재 `SubwayNetworkServiceTest`, `BusNetworkServiceTest`, `SubwayApiQueryServiceTest`, `PaceApplicationTests` 등은 `@Disabled` 상태다.
- MySQL·Redis·실제 외부 API를 사용하는 테스트는 필요한 환경 변수와 서비스가 준비된 경우에만 실행한다.
- 테스트에서 실제 계정, API key, `.env` 값을 커밋하지 않는다.
- 외부 의존 테스트를 실행하지 않았다면 결과를 통과로 기록하지 않는다.

### Repository와 Controller 테스트

- 현재 repository 전용 `@DataJpaTest`와 controller 전용 `@WebMvcTest`는 테스트 inventory에 없다.
- 새 repository query 테스트는 `@DataJpaTest`를 우선 검토한다.
- 새 HTTP 계약 테스트는 `@WebMvcTest`와 `MockMvc`를 우선 검토한다.
- 인증 필터를 끈 controller 테스트는 HTTP 계약만 검증하며 보안 동작을 검증하지 않는다.
- 현재 Spring Boot 4 테스트 의존성에 맞는 mock 어노테이션을 확인하고, 프로젝트에 이미 사용하지 않는 방식을 무리하게 도입하지 않는다.

## 테스트 구조

- Arrange–Act–Assert 순서를 사용한다.
- 새 테스트의 주석은 `// given`, `// when`, `// then`으로 짧게 표시한다.
- 한 테스트는 하나의 observable behavior 또는 하나의 실패 경로에 집중한다.
- 테스트 메서드는 `<method>_<scenario>_<expectedResult>` 형식을 우선한다.
- `@DisplayName`은 실제 사용자·도메인 결과를 짧게 설명한다.

```java
// given
given(busInfoRepository.findCorrectBusRoute(...))
        .willReturn(Optional.empty());

// when / then
assertThatThrownBy(() -> service.searchStartStationInfo(...))
        .isInstanceOf(TransitException.class)
        .hasMessageContaining(TransitErrorCode.TRANSIT_BUS_NOT_FOUND.getMessage());
```

예외 테스트에서는 예외 타입과 도메인 code처럼 외부에서 관찰 가능한 계약을 검증한다. private 구현 세부사항을 검증하지 않는다.

## Mockito 규칙

- 시나리오에 사용되는 호출만 stub한다.
- 계약상 중요한 인자는 구체값 또는 `eq()`를 사용한다.
- 의미 없는 인자에만 `any()`를 사용한다.
- collaborator 호출 자체가 계약인 경우에만 `verify`와 `never()`를 사용한다.
- 기계적인 `verifyNoMoreInteractions`와 `lenient()` 사용을 피한다.

## 데이터와 결정성

- fixture는 최소한으로 명시한다.
- 시간에 의존하는 테스트는 고정값을 사용한다.
- `Thread.sleep()`으로 동기화하지 않는다.
- 정적 버스·지하철 JSON/Excel 데이터에 의존하는 테스트는 fixture 경로와 자료 버전을 확인한다.
- DB 조회·제약조건을 검증할 때는 persistence context flush/clear 필요 여부를 확인한다.

## 실행과 기록

- 수정 중에는 가장 가까운 focused test를 먼저 실행한다.
- 기본 검증은 외부 인프라가 필요 없는 관련 테스트와 `git diff --check`다.
- 전체 `./gradlew test`, `clean build`, Docker·MySQL·Redis 의존 테스트는 사용자가 요청했거나 작업 범위상 필요할 때 실행한다.
- 실행하지 않은 테스트는 미실행 사유와 남은 위험을 handoff에 기록한다.
- 실패한 테스트를 비활성화하거나 assertion을 약화해 통과시키지 않는다.
