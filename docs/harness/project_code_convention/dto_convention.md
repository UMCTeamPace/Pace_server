# Spring Boot DTO Convention

## 1. 기본 원칙

DTO는 API 요청·응답, 외부 API 원본 응답, persistence 조회 결과처럼 경계가 다른 데이터를 분리한다. Entity를 Controller의 요청·응답 타입으로 직접 사용하지 않는다.

현재 프로젝트는 Java record와 Lombok 기반 class를 함께 사용한다. 기존 DTO를 일괄적으로 record 또는 단일 명명 규칙으로 변경하지 않는다.

## 2. 패키지 구조

```text
src/main/java/com/example/pace/domain/{domain}/dto/
├── request/
└── response/
```

예시:

```text
domain/transit/dto/request/BusInfoReqDTO.java
domain/transit/dto/response/BusInfoResDTO.java
domain/schedule/dto/request/ScheduleReqDto.java
domain/schedule/dto/response/ScheduleResDto.java
```

외부 API 원본 응답처럼 특정 infrastructure에 강하게 결합된 DTO는 해당 domain의 `infrastructure/dto`에 둘 수 있다. 현재 Google Directions 원본 응답이 이 방식을 사용한다.

## 3. 명명과 형태

- 기존 프로젝트에는 `ReqDTO`/`ResDTO`와 `ReqDto`/`ResDto`가 함께 존재한다. 새 파일은 같은 도메인·기능의 기존 표기를 따른다.
- 여러 관련 타입을 묶는 기존 패턴은 `public class AuthReqDTO` 안의 `public static class`를 사용한다.
- 단일 요청·응답 타입은 `OnboardingReqDTO`, `SettingResponseDTO`처럼 최상위 class/record로 둘 수 있다.
- record는 불변 입력과 단순 결과에 적합할 때 사용한다. 현재 `OnboardingReqDTO`, `RouteSaveReqDto` 등이 record다.
- 외부 JSON 역직렬화와 부분 수정 요청은 현재처럼 Lombok `@Getter`, `@Setter`, `@NoArgsConstructor` class를 사용할 수 있다.
- 응답 객체는 현재 `@Getter`, `@Builder`, `@AllArgsConstructor` 조합이 주로 사용된다.

## 4. 요청 DTO

- 요청 타입은 `dto/request`에 둔다.
- `jakarta.validation`을 사용한다.
- 필수값·길이·범위·형식처럼 입력값만으로 판단 가능한 제약은 DTO에 선언한다.
- 중첩 DTO에는 필요한 경우 `@Valid`를 적용한다.
- 중복·존재 여부·권한·상태·도메인 간 관계 검증은 Service에서 처리한다.
- Controller가 `@RequestBody` 또는 `@ModelAttribute`에 `@Valid`를 적용하는지 함께 확인한다.
- PATCH처럼 일부 필드만 받는 요청은 선택 필드를 허용하되, 실제 변경 규칙은 Service에서 처리한다.

```java
public record OnboardingReqDTO(
        @NotNull Boolean isReminderActive,
        @Min(0) @Max(60) Integer earlyArrivalTime,
        @NotNull String calendarId,
        @Valid List<AlarmConfig> alarms
) {
}
```

## 5. 응답 DTO

- 응답 타입은 `dto/response`에 둔다.
- 민감한 token·password·외부 API 키를 의도하지 않게 포함하지 않는다.
- 컬렉션 응답은 API 계약에 따라 빈 목록을 반환할지 nullable인지 명확히 한다.
- QueryDSL projection처럼 repository가 직접 채우는 조회 DTO는 일반 API 응답 DTO와 구분한다. 현재 `PlaceGroupQueryDTO`가 그 예다.
- 시간·enum·외부 필드의 표현 변환은 Converter 또는 명시적인 변환 계층에서 처리한다.

## 6. 변환과 Entity 경계

- Entity를 Controller에 직접 반환하지 않는다.
- 단순 변환은 Converter에 위임한다.
- 현재 일부 DTO에는 `from`/`of` 정적 메서드가 남아 있다. 새 DTO에 변환 책임을 추가하기보다 기존 도메인의 변환 방식과 호환성을 우선 확인한다.
- DTO는 Repository, Service, 외부 API client에 의존하지 않는다.

## 7. 금지 사항

- 요청과 응답을 의미 없이 하나의 DTO에 섞는 것
- DTO에서 DB 조회·권한 검증·비즈니스 처리를 수행하는 것
- DTO에 Entity 연관관계를 그대로 노출하는 것
- `javax.validation` 사용
- 기존 API 필드명과 호환성 검토 없이 DTO 이름·필드·JSON 구조를 변경하는 것
