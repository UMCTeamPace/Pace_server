package com.example.pace.domain.member.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceGroupResDTO {
    // querydsl 조회 결고를 직접 받을 DTO
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PlaceGroupQueryDTO {
        private Long groupId;
        private String groupName;
        private String groupColor;
        private LocalDateTime createdAt;
        private Long placeCount;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PlaceGroupDTO {
        private Long groupId;
        private String groupName;
        private String groupColor;
        private String createdAt;
        private Long placeCount;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PlaceGroupListDTO {
        private List<PlaceGroupDTO> placeGroupList;
        private Integer listSize;
    }
}
