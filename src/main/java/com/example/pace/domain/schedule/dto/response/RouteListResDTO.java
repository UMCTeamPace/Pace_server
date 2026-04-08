package com.example.pace.domain.schedule.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteListResDTO {
    private List<RouteApiResDto> routeApiResDtoList;
}
