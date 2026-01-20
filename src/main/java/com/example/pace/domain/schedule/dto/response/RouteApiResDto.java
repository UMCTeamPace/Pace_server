package com.example.pace.domain.schedule.dto.response;

import com.example.pace.domain.schedule.dto.response.info.RouteDetailInfoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

//경로 저장 DTO api->서버
@Getter
@AllArgsConstructor
public class RouteApiResDto {
    private List<RouteDetailInfoDTO> routeDetailInfoDTOList;
}
