package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

// 경로 저장 DTO 서버->유저

@Getter
@AllArgsConstructor
public class RouteSaveResDTO {
    private Long routeId;               // 저장된 경로 식별자
    private List<RouteDetailInfoDTO> routes; // 경로 상세
}
