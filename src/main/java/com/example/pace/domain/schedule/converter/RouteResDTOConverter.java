package com.example.pace.domain.schedule.converter;


import com.example.pace.domain.schedule.dto.response.RouteApiResDto;
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

public class RouteResDTOConverter {

    // 한국 시간대 기준 (필요에 따라 변경 가능)
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    public static RouteApiResDto toRouteApiResDto(GoogleDirectionApiResponse apiResponse) {
        if (apiResponse == null || apiResponse.getRoutes() == null || apiResponse.getRoutes().isEmpty()) {
            return RouteApiResDto.builder().build(); // 빈 객체 반환 혹은 예외 처리
        }

        // 첫 번째 경로 사용
        GoogleDirectionApiResponse.Route route = apiResponse.getRoutes().get(0);
        if (route.getLegs() == null || route.getLegs().isEmpty()) {
            return RouteApiResDto.builder().build();
        }

        GoogleDirectionApiResponse.Leg firstLeg = route.getLegs().get(0);
        List<GoogleDirectionApiResponse.Step> steps = firstLeg.getSteps();

        List<RouteDetailInfoResDTO> details = new ArrayList<>();

        // Sequence 부여를 위해 for문 혹은 AtomicInteger 사용
        for (int i = 0; i < steps.size(); i++) {
            details.add(toRouteDetailInfoResDTO(steps.get(i), i + 1));
        }

        return RouteApiResDto.builder()
                .totalDistance(safeInt(firstLeg.getDistance().getValue()))
                .totalTime(safeInt(firstLeg.getDuration().getValue()))
                .departureTime(epochToLocalDateTime(
                        firstLeg.getDepartureTime() != null ? firstLeg.getDepartureTime().getValue() : null))
                .arrivalTime(epochToLocalDateTime(
                        firstLeg.getArrivalTime() != null ? firstLeg.getArrivalTime().getValue() : null))
                .routeDetailInfoResDTOList(details)
                .build();
    }

    private static RouteDetailInfoResDTO toRouteDetailInfoResDTO(GoogleDirectionApiResponse.Step step, int sequence) {
        RouteDetailInfoResDTO.RouteDetailInfoResDTOBuilder builder = RouteDetailInfoResDTO.builder()
                .sequence(sequence) // 순서 주입
                .startLat(BigDecimal.valueOf(step.getStartLocation().getLat()))
                .startLng(BigDecimal.valueOf(step.getStartLocation().getLng()))
                .endLat(BigDecimal.valueOf(step.getEndLocation().getLat()))
                .endLng(BigDecimal.valueOf(step.getEndLocation().getLng()))
                .duration(safeInt(step.getDuration().getValue()))
                .distance(safeInt(step.getDistance().getValue()))
                .description(step.getHtmlInstructions());

        // TRANSIT 모드일 때만 상세 정보 매핑
        if ("TRANSIT".equalsIgnoreCase(step.getTravelMode()) && step.getTransitDetails() != null) {
            // Polyline은 보통 Step 레벨에 존재함
            String polyline = (step.getEncodedPolyline() != null) ? step.getEncodedPolyline().getPoints() : null;
            builder.transitDetail(toTransitRouteDetailInfoResDTO(step.getTransitDetails(), polyline));
        }

        return builder.build();
    }

    private static TransitRouteDetailInfoResDTO toTransitRouteDetailInfoResDTO(
            GoogleDirectionApiResponse.TransitDetails transit, String polyline) {

        GoogleDirectionApiResponse.EncodedLine line = transit.getEncodedLine();
        String vehicleType = (line != null && line.getVehicle() != null) ? line.getVehicle().getType() : null;

        // Google 응답에서 Double 좌표 추출
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

                // [핵심 수정] Double이 null이 아닐 때만 BigDecimal로 변환
                .locationLat(lat != null ? BigDecimal.valueOf(lat) : null)
                .locationLng(lng != null ? BigDecimal.valueOf(lng) : null)

                .points(polyline)
                .headsign(transit.getHeadsign())
                .build();


    }

    // Null Safety Helper
    private static Integer safeInt(Long value) {
        return value != null ? Math.toIntExact(value) : 0;
    }


    private static LocalDateTime epochToLocalDateTime(Long epochSeconds) {
        if (epochSeconds == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSeconds), ZONE_ID);
    }


    // 교통정보 매핑
    private static TransitType mapTransitType(String vehicleType) {
        if (vehicleType == null) {
            return TransitType.WALK;
        }
        // 구글 API 응답값에 맞춰 케이스 추가 필요
        switch (vehicleType.toUpperCase()) {
            case "BUS":
            case "INTERCITY_BUS":
            case "TROLLEYBUS":
                return TransitType.BUS;
            case "SUBWAY":
            case "METRO":
            case "RAIL":
            case "TRAM":
            case "HEAVY_RAIL":
                return TransitType.SUBWAY;
            default:
                return TransitType.WALK; // 혹은 TransitType.OTHER
        }
    }
}