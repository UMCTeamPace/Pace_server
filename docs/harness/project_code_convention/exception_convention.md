# Exception Convention

## 1. 현재 예외 구조

공통 예외 기반 타입과 전역 처리기는 다음 위치에 있다.

```text
global/apiPayload/
├── code/
│   ├── BaseErrorCode.java
│   ├── BaseSuccessCode.java
│   ├── GeneralErrorCode.java
│   └── GeneralSuccessCode.java
├── exception/GeneralException.java
└── handler/GeneralExceptionAdvice.java
```

도메인 코드는 현재 `error/`, `success/` 하위 디렉터리로 나누지 않고 flat한 `exception/code`에 둔다.

```text
domain/{domain}/exception/
├── {Domain}Exception.java
└── code/
    ├── {Domain}ErrorCode.java
    └── {Domain}SuccessCode.java
```

## 2. ErrorCode와 SuccessCode

- ErrorCode는 `BaseErrorCode`, SuccessCode는 `BaseSuccessCode`를 구현한다.
- enum은 HTTP status, message, application code를 제공한다.
- application code는 기존 도메인 규칙과 공개 API 호환성을 우선한다.
- 새 코드를 추가할 때 같은 도메인·HTTP status의 기존 번호와 중복하지 않는다.
- 공개된 code, message, HTTP status를 임의로 변경하지 않는다.
- 현재 enum 상수명은 `*_OK`, `*_SUCCESS`, `*_FOUND_OK` 등으로 혼재하므로 새 상수는 같은 도메인의 기존 명명과 맞춘다. 모든 상수에 `_SUCCESS`를 강제하지 않는다.

## 3. 도메인 예외

새 비즈니스 예외는 `GeneralException`을 상속하고 해당 도메인의 ErrorCode를 받는다.

```java
public class TransitException extends GeneralException {

    public TransitException(TransitErrorCode errorCode) {
        super(errorCode);
    }
}
```

Service·Component는 상황에 맞는 도메인 ErrorCode로 예외를 발생시킨다. Controller에서 예외를 잡아 성공·실패 응답으로 직접 바꾸지 않는다.

## 4. 전역 처리

`GeneralExceptionAdvice`는 다음을 처리한다.

- `GeneralException`: 예외의 ErrorCode HTTP status와 `ApiResponse.onFailure` 사용
- `BindException`: validation 실패를 `GeneralErrorCode.BAD_REQUEST`로 변환
- 그 외 `Exception`: `GeneralErrorCode.INTERNAL_SERVER_ERROR`로 변환

예외 응답의 JSON은 공통 `ApiResponse` 필드인 `isSuccess`, `code`, `message`, `result`를 따른다. `ResponseEntity`는 advice가 HTTP status를 설정하는 전역 경계에 사용된다.

## 5. 현재 예외와 새 코드 기준

- `auth`, `member`, `transit`의 주요 도메인 예외는 `GeneralException` 기반이다.
- `schedule/exception/ScheduleException`은 현재 `RuntimeException`을 직접 상속하는 legacy 예외이며 `GeneralExceptionAdvice`의 도메인 예외 경계와 다르다.
- 새 schedule 비즈니스 예외는 `GeneralException` 기반으로 작성한다. 기존 `ScheduleException`을 변경할 때는 호출부와 응답 호환성을 함께 확인한다.
- `ScheduleCommandService` 일부는 `GeneralException(GeneralErrorCode 또는 ScheduleErrorCode)`를 직접 생성한다. 새 코드에서는 의미가 있는 도메인 Exception wrapper를 우선한다.

## 6. 금지 사항

- Controller에서 도메인 예외를 `try-catch`해 직접 응답하는 것
- Service에서 `ResponseStatusException`을 비즈니스 예외 대용으로 사용하는 것
- 새 비즈니스 오류를 임의의 문자열이나 `IllegalArgumentException`으로만 표현하는 것
- ErrorCode의 message와 application code를 여러 계층에 중복 작성하는 것
- 다른 도메인의 ErrorCode를 도메인 Exception에 전달하는 것
- 비밀번호, token, API key 등 민감 정보를 예외 message나 로그에 포함하는 것
