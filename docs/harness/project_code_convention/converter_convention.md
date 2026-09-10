# Converter Convention

## 목적

Converter는 DTO, Entity, QueryDSL projection, 외부 API 응답 사이의 표현을 변환한다. 변환에 필요한 값은 Service나 호출자가 조회해 인자로 전달한다.

## 패키지와 현재 구현

```text
src/main/java/com/example/pace/domain/{domain}/converter/{Domain}Converter.java
```

현재 `auth`, `member`, `schedule` 도메인이 Converter를 사용한다. 클래스는 대체로 static 메서드를 제공하며, 일부는 `private` 생성자를 갖고 일부 legacy class는 public 기본 생성자를 남겨두고 있다.

## 책임

Converter가 담당하는 작업:

- Request DTO → Entity
- Entity 또는 projection → Response DTO
- 외부 API 응답 → 내부 DTO
- 여러 이미 조회된 값을 하나의 응답으로 조립
- 목록·페이지 변환
- 날짜·시간·필드명처럼 표현 형식이 다른 값의 변환

Converter가 담당하지 않는 작업:

- Repository 조회 또는 Service 호출
- 권한·중복·존재 여부·상태 판단
- 비밀번호 암호화와 외부 API 호출
- transaction 처리
- HTTP 응답 생성
- 기존 Entity의 상태 변경

`OnboardingConverter.toAlarmMap`처럼 입력값을 정규화하는 순수 변환은 허용한다. 다만 허용 가능한 값인지 판단하는 도메인 규칙은 Service에 둔다.

## 새 Converter 규칙

- 상태를 갖지 않는 `final class`와 private 생성자를 우선한다.
- 변환 메서드는 static으로 작성한다.
- 메서드명은 `toEntity`, `toResponse`, `toResult`, `toDetail`, `toList`처럼 결과를 드러낸다.
- Entity 생성은 builder 등 현재 Entity의 생성 방식을 사용하며 public setter로 조립하지 않는다.
- 연관 Entity, 암호화된 값, 외부 처리 결과는 인자로 전달받는다.
- null과 빈 컬렉션 정책은 해당 API 계약에 맞춰 명시적으로 처리한다.

## 현재 코드와의 호환성

- 기존 메서드명 `toPlaceGroupDTO`, `toPlaceDTO`, `toScheduleResDto` 등은 호출부가 있는 동안 임의로 이름을 바꾸지 않는다.
- 일부 응답 DTO에 `from`/`of`가 존재하지만, 새 변환 로직을 DTO에 계속 추가하지 않는다.
- QueryDSL projection을 응답 DTO로 바꾸는 경우 projection의 필드 의미와 null 가능성을 확인한다.

## 체크리스트

- [ ] DB·Service·외부 Client 의존성이 없는가?
- [ ] 변환에 필요한 값이 호출자에서 명시적으로 전달되는가?
- [ ] 비즈니스 예외·권한 판단을 수행하지 않는가?
- [ ] Entity를 API 응답으로 직접 노출하지 않는가?
- [ ] 기존 API 필드명과 날짜·enum 표현을 보존하는가?
