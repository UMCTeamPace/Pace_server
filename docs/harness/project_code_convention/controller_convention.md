# Controller Convention

## 목적

Controller는 HTTP 진입점으로서 요청을 받고 Service 또는 Component를 호출한 뒤 응답을 반환한다. 비즈니스 규칙, Repository 조회, 외부 API 호출은 Controller에 두지 않는다.

## 패키지와 클래스

```text
{domain}/controller/
├── {Domain}Controller.java
└── docs/{Domain}ControllerDocs.java
```

현재 `auth`, `member`, `transit`와 일부 `schedule` Controller는 `controller/docs`의 Swagger 인터페이스를 구현한다. 새 API는 기존 도메인의 방식을 따르고, 공개 API라면 Docs 인터페이스에 문서화를 둔다.

기본 어노테이션은 `@RestController`, `@RequestMapping`, 생성자 주입이며, 현재 프로젝트는 Lombok `@RequiredArgsConstructor`를 사용한다.

## 책임

Controller가 담당하는 작업:

- HTTP method와 URL 매핑
- `@PathVariable`, `@RequestParam`, `@RequestBody`, `@ModelAttribute` 수신
- `@Valid`를 이용한 요청 DTO 검증 연결
- `@AuthenticationPrincipal CustomUserDetails`에서 회원 식별자 추출
- 적절한 Service 또는 Component 호출
- `ApiResponse<T>`와 SuccessCode를 사용한 성공 응답 생성

Controller가 담당하지 않는 작업:

- Repository 직접 호출
- Entity 생성·수정·삭제
- 복잡한 DTO 변환
- 권한·중복·존재 여부 같은 비즈니스 판단
- 트랜잭션 처리
- 도메인 예외를 `try-catch`로 직접 응답 변환

## 성공 응답

새 API의 기본 반환 타입은 `ApiResponse<T>`다.

```java
return ApiResponse.onSuccess(
        TransitSuccessCode.TRANSIT_BUS_OK,
        busNetworkService.searchStartStationInfo(
                request.getLineName(),
                request.getStartStation(),
                request.getEndStation()
        )
);
```

성공 메시지와 코드를 Controller에 문자열로 직접 작성하지 않는다. 생성 성공처럼 HTTP 상태가 `201 Created`여야 하는 경우에는 기존 프로젝트처럼 `@ResponseStatus` 또는 필요한 범위의 `ResponseEntity`를 사용하되, 도메인의 SuccessCode와 응답 body 계약을 함께 유지한다.

## 요청 검증과 인증

- `@RequestBody` DTO에는 `@Valid`를 적용한다.
- `@ModelAttribute` DTO도 해당 endpoint가 validation을 요구하면 `@Valid`를 적용한다.
- 단순 형식·필수값·범위 검증은 DTO에서 처리한다.
- DB 조회가 필요한 존재 여부·소유권·상태 검증은 Service에서 처리한다.
- 인증이 필요한 endpoint는 `@AuthenticationPrincipal CustomUserDetails`를 사용한다.

## Docs 인터페이스

Docs 인터페이스에는 Swagger/OpenAPI 어노테이션과 Controller 메서드 계약만 둔다.

- `@Tag`
- `@Operation`
- `@ApiResponses`
- `@Parameter`
- 요청·응답 스키마 설명

구현 Controller와 Docs 인터페이스의 메서드명, 매개변수, 반환 타입을 일치시킨다. Docs 인터페이스에서 구현 로직이나 Repository·Service 호출을 작성하지 않는다.

## 기존 예외와 새 코드 기준

현재 다음 legacy endpoint는 새 규칙과 다르다.

- `SavedPlaceController`와 `ScheduleController`: `ResponseEntity<ApiResponse<...>>`
- `RouteController`: `ResponseEntity<RouteListResDTO>`
- `HealthCheckController`: 문자열 `OK`

이 문서는 기존 endpoint의 일괄 변경을 요구하지 않는다. 새 endpoint는 `ApiResponse<T>`를 우선 사용하고, 기존 endpoint를 수정할 때는 해당 API의 호환성 영향을 먼저 확인한다.
