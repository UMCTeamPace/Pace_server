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
import java.util.List;
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

        flattenSteps(firstLeg.getSteps(), details, sequence);

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

    //    public static RouteApiResDto toRouteApiResDto(GoogleDirectionApiResponse apiResponse) {
    //        if (apiResponse == null || apiResponse.getRoutes() == null || apiResponse.getRoutes().isEmpty()) {
    //            return RouteApiResDto.builder().build();
    //        }
    //
    //        GoogleDirectionApiResponse.Route route = apiResponse.getRoutes().get(0);
    //        if (route.getLegs() == null || route.getLegs().isEmpty()) {
    //            return RouteApiResDto.builder().build();
    //        }
    //
    //        GoogleDirectionApiResponse.Leg firstLeg = route.getLegs().get(0);
    //        List<RouteDetailInfoResDTO> details = new ArrayList<>();
    //        AtomicInteger sequence = new AtomicInteger(0);
    //
    //        // 재귀적으로 모든 스텝을 평탄화
    //        flattenSteps(firstLeg.getSteps(), details, sequence);
    //
    //        return RouteApiResDto.builder()
    //                .totalDistance(firstLeg.getDistance() != null ? safeInt(firstLeg.getDistance().getValue()) : 0)
    //                .totalTime(firstLeg.getDuration() != null ? safeInt(firstLeg.getDuration().getValue()) : 0)
    //                .departureTime(epochToLocalDateTime(
    //                        firstLeg.getDepartureTime() != null ? firstLeg.getDepartureTime().getValue() : null))
    //                .arrivalTime(epochToLocalDateTime(
    //                        firstLeg.getArrivalTime() != null ? firstLeg.getArrivalTime().getValue() : null))
    //                .routeDetailInfoResDTOList(details)
    //                .build();
    //    }

    // 재귀적으로 스텝을 평탄화하는 메서드
    private static void flattenSteps(List<GoogleDirectionApiResponse.Step> steps,
                                     List<RouteDetailInfoResDTO> resultList,
                                     AtomicInteger sequence) {
        if (steps == null || steps.isEmpty()) {
            return;
        }

        for (GoogleDirectionApiResponse.Step step : steps) {
            // 현재 스텝을 결과에 추가
            resultList.add(toRouteDetailInfoResDTO(step, sequence.incrementAndGet()));

            // 자식 스텝이 있으면 재귀 처리
            if (step.getSteps() != null && !step.getSteps().isEmpty()) {
                flattenSteps(step.getSteps(), resultList, sequence);
            }
        }
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

    public static LocalDateTime epochToLocalDateTime(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZONE_ID);
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
            case "BUS" -> TransitType.BUS;
            case "SUBWAY" -> TransitType.SUBWAY;
            case "WALKING" -> TransitType.WALK;
            default -> TransitType.UNDEFINED;
        };
    }
}