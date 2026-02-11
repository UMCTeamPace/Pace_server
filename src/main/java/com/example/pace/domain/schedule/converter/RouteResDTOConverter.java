package com.example.pace.domain.schedule.converter;


import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
import com.example.pace.domain.schedule.dto.response.RouteListResDTO;
import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoResDTO;
import com.example.pace.domain.schedule.dto.response.info.TransitRouteDetailInfoResDTO;
import com.example.pace.domain.schedule.enums.TransitType;
import com.example.pace.domain.schedule.infrastructure.dto.GoogleDirectionApiResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RouteResDTOConverter {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private RouteResDTOConverter() {
        // 유틸리티 클래스 인스턴스 방지
    }

    public static RouteListResDTO toRouteListResDTO(GoogleDirectionApiResponse apiResponse) {

        if (apiResponse == null || apiResponse.getRoutes() == null) {
            return RouteListResDTO.builder()
                    .routeApiResDtoList(List.of())
                    .build();
        }

        List<RouteApiResDto> routeList = apiResponse.getRoutes().stream()
                .limit(10) //
                .map(RouteResDTOConverter::convertSingleRoute)
                .toList();

        return RouteListResDTO.builder()
                .routeApiResDtoList(routeList)
                .build();
    }

    private static RouteApiResDto convertSingleRoute(GoogleDirectionApiResponse.Route route) {

        if (route.getLegs() == null || route.getLegs().isEmpty()) {
            return RouteApiResDto.builder().build();
        }

        GoogleDirectionApiResponse.Leg firstLeg = route.getLegs().get(0);

        List<RouteDetailInfoResDTO> details = new ArrayList<>();
        AtomicInteger sequence = new AtomicInteger(0);

        Set<String> walkingPointsSet = new HashSet<>();
        flattenSteps(firstLeg.getSteps(), details, sequence, walkingPointsSet);

        return RouteApiResDto.builder()
                .totalDistance(firstLeg.getDistance() != null ? safeInt(firstLeg.getDistance().getValue()) : 0)
                .totalTime(firstLeg.getDuration() != null ? safeInt(firstLeg.getDuration().getValue()) : 0)
                .departureTime(epochToLocalDateTime(
                        firstLeg.getDepartureTime() != null ? firstLeg.getDepartureTime().getValue() : null))
                .arrivalTime(epochToLocalDateTime(
                        firstLeg.getArrivalTime() != null ? firstLeg.getArrivalTime().getValue() : null))
                .routeDetailInfoResDTOList(details)
                .build();
    }

    // 재귀적으로 스텝을 평탄화하는 메서드
    private static void flattenSteps(List<GoogleDirectionApiResponse.Step> steps,
                                     List<RouteDetailInfoResDTO> resultList,
                                     AtomicInteger sequence,
                                     Set<String> walkingPointsSet) {

        if (steps == null || steps.isEmpty()) {
            return;
        }

        RouteDetailInfoResDTO walkingBuffer = null;

        for (GoogleDirectionApiResponse.Step step : steps) {
            String travelMode = step.getTravelMode();

            // ✅ 무의미한 step 제거
            if (step.getDistance() != null
                    && step.getDistance().getValue() == 0
                    && !"TRANSIT".equalsIgnoreCase(travelMode)) {
                continue;
            }

            // ================================
            // 🚶 WALKING이면 합치기 (Setter 없이 새 객체 생성)
            // ================================
            if ("WALKING".equalsIgnoreCase(travelMode)) {
                if (step.getHtmlInstructions() == null) {
                    continue;
                }

                String desc = step.getHtmlInstructions().replaceAll("<[^>]*>", "");
                if (desc.contains("출구")) {
                    continue;
                }

                RouteDetailInfoResDTO currentWalking = toRouteDetailInfoResDTO(step, 0);

                if (walkingBuffer == null) {
                    walkingBuffer = currentWalking;
                } else {
                    // 핵심: 기존 값을 가져와서 새 Builder로 객체를 다시 생성 (re-assign)
                    walkingBuffer = RouteDetailInfoResDTO.builder()
                            .startLat(walkingBuffer.getStartLat())
                            .startLng(walkingBuffer.getStartLng())
                            .endLat(currentWalking.getEndLat()) // 끝 좌표 갱신
                            .endLng(currentWalking.getEndLng())
                            .distance(walkingBuffer.getDistance() + currentWalking.getDistance()) // 거리 합산
                            .duration(walkingBuffer.getDuration() + currentWalking.getDuration()) // 시간 합산
                            .points(walkingBuffer.getPoints())
                            .description(walkingBuffer.getDescription())
                            .build();
                }
                continue;
            }

            // ================================
            // 🚇 TRANSIT이나 다른 수단 만나면 도보 flush
            // ================================
            if (walkingBuffer != null) {
                // "도보 이동" 문구와 최종 sequence를 입혀서 리스트에 추가
                resultList.add(finalizeWalking(walkingBuffer, sequence.incrementAndGet()));
                walkingBuffer = null;
            }

            if ("TRANSIT".equalsIgnoreCase(travelMode)) {
                resultList.add(toRouteDetailInfoResDTO(step, sequence.incrementAndGet()));

                if (step.getSteps() != null && !step.getSteps().isEmpty()) {
                    flattenSteps(step.getSteps(), resultList, sequence, walkingPointsSet);
                }
            } else {
                resultList.add(toRouteDetailInfoResDTO(step, sequence.incrementAndGet()));
            }
        }

        // ================================
        // 🚶 마지막 잔여 도보 flush
        // ================================
        if (walkingBuffer != null) {
            resultList.add(finalizeWalking(walkingBuffer, sequence.incrementAndGet()));
        }
    }

    /**
     * 도보 버퍼를 최종 확정할 때 사용하는 헬퍼 메서드
     */
    private static RouteDetailInfoResDTO finalizeWalking(RouteDetailInfoResDTO buffer, int seq) {
        return RouteDetailInfoResDTO.builder()
                .sequence(seq)
                .startLat(buffer.getStartLat())
                .startLng(buffer.getStartLng())
                .endLat(buffer.getEndLat())
                .endLng(buffer.getEndLng())
                .distance(buffer.getDistance())
                .duration(buffer.getDuration())
                .description(buffer.getDescription()) // 공통 문구 할당
                .points(buffer.getPoints())
                .build();
    }


    private static RouteDetailInfoResDTO toRouteDetailInfoResDTO(GoogleDirectionApiResponse.Step step, int sequence) {
        // 좌표 정보 가져오기 (Null 체크 강화)
        BigDecimal startLat =
                (step.getStartLocation() != null) ? BigDecimal.valueOf(step.getStartLocation().getLat()) : null;
        BigDecimal startLng =
                (step.getStartLocation() != null) ? BigDecimal.valueOf(step.getStartLocation().getLng()) : null;
        BigDecimal endLat = (step.getEndLocation() != null) ? BigDecimal.valueOf(step.getEndLocation().getLat()) : null;
        BigDecimal endLng = (step.getEndLocation() != null) ? BigDecimal.valueOf(step.getEndLocation().getLng()) : null;

        // description에서 HTML 태그 제거 (예: <b>서울역</b> -> 서울역)
        String rawDescription = step.getHtmlInstructions();
        String cleanDescription = (rawDescription != null) ? rawDescription.replaceAll("<[^>]*>", "") : null;
        String polyline = step.getEncodedPolyline() != null ? step.getEncodedPolyline().getPoints() : null;

        RouteDetailInfoResDTO.RouteDetailInfoResDTOBuilder builder = RouteDetailInfoResDTO.builder()
                .sequence(sequence)
                .startLat(startLat)
                .startLng(startLng)
                .endLat(endLat)
                .endLng(endLng)
                .duration(step.getDuration() != null ? safeInt(step.getDuration().getValue()) : 0)
                .distance(step.getDistance() != null ? safeInt(step.getDistance().getValue()) : 0)
                .description(cleanDescription)
                .points(polyline);

        // TRANSIT 모드 상세 매핑
        if ("TRANSIT".equalsIgnoreCase(step.getTravelMode()) && step.getTransitDetails() != null) {
            builder.transitDetail(toTransitRouteDetailInfoResDTO(step.getTransitDetails(), polyline));
        }

        return builder.build();
    }


    private static TransitRouteDetailInfoResDTO toTransitRouteDetailInfoResDTO(
            GoogleDirectionApiResponse.TransitDetails transit, String polyline) {

        GoogleDirectionApiResponse.EncodedLine line = transit.getEncodedLine();
        String vehicleType = (line != null && line.getVehicle() != null) ? line.getVehicle().getType() : null;

        Double lat = null;
        Double lng = null;

        if (transit.getArrivalStop() != null && transit.getArrivalStop().getEncodedLocation() != null) {
            lat = transit.getArrivalStop().getEncodedLocation().getLat();
            lng = transit.getArrivalStop().getEncodedLocation().getLng();
        }

        return TransitRouteDetailInfoResDTO.builder()
                .transitType(mapTransitType(vehicleType))
                .lineName(line != null ? line.getName() : null)
                .lineColor(line != null ? line.getColor() : null)
                .stopCount(transit.getNumStops() != null ? transit.getNumStops().intValue() : 0)
                .departureStop(transit.getDepartureStop() != null ? transit.getDepartureStop().getEncodedName() : null)
                .arrivalStop(transit.getArrivalStop() != null ? transit.getArrivalStop().getEncodedName() : null)
                .departureTime(epochToLocalDateTime(
                        transit.getDepartureTime() != null ? transit.getDepartureTime().getValue() : null))
                .arrivalTime(epochToLocalDateTime(
                        transit.getArrivalTime() != null ? transit.getArrivalTime().getValue() : null))
                .locationLat(lat != null ? BigDecimal.valueOf(lat) : null)
                .locationLng(lng != null ? BigDecimal.valueOf(lng) : null)
                .shortName(line != null ? line.getName() : null)
                .headsign(transit.getHeadsign())
                .build();
    }

    private static Integer safeInt(Long value) {
        if (value == null) {
            return 0;
        }
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return value.intValue();
    }

    // epoch 변환 관련 헬퍼들
    // Google API에서 제공하는 값이 초(seconds)인지 밀리초(milliseconds)인지 확실하지 않을 때를 대비해 자동 감지 및 정규화합니다.
    private static long normalizeEpochSeconds(Long epochLikeValue) {
        if (epochLikeValue == null) {
            return -1L;
        }
        // 1970-01-01 기준: 초는 현재 약 1_700_000_000 수준, 밀리초는 약 1_700_000_000_000 수준입니다.
        // 1_000_000_000_000 (1e12) 보다 크면 밀리초로 간주하여 1000으로 나눕니다.
        if (epochLikeValue > 1_000_000_000_000L) {
            return epochLikeValue / 1000L;
        }
        return epochLikeValue;
    }

    public static LocalDateTime epochToLocalDateTime(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        long normalized = normalizeEpochSeconds(epochSeconds);
        if (normalized < 0) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(normalized), ZONE_ID);
    }

    public static Long localDateTimeToEpoch(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(ZONE_ID).toEpochSecond();
    }

    private static TransitType mapTransitType(String vehicleType) {
        if (vehicleType == null) {
            return TransitType.UNDEFINED;
        }
        return switch (vehicleType.toUpperCase()) {

            // BUS 계열 -> 그냥 일단 때려넣었습니다
            case "BUS", "INTERCITY_BUS", "TROLLEYBUS" -> TransitType.BUS;

            //  SUBWAY 계열
            case "SUBWAY", "METRO_RAIL", "HEAVY_RAIL", "COMMUTER_TRAIN" -> TransitType.SUBWAY;

            //  TRAIN 계열
            case "RAIL", "TRAIN" -> TransitType.TRAIN;

            default -> TransitType.UNDEFINED;
        };
    }
}
