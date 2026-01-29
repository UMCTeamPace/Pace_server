package com.example.pace.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceGroupResDTO {
    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PlaceGroupDTO {
        private Long groupId;
        private String groupName;
        private String groupColor;
        private String createdAt;
    }
}
