package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoResDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//경로 저장 DTO api->서버
@Getter
@Builder
@AllArgsConstructor
public class RouteApiResDto {
    private List<RouteDetailInfoResDTO> routeDetailInfoDTOList;
}
