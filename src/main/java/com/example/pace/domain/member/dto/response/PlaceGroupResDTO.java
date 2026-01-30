package com.example.pace.domain.member.dto.response;

import java.util.List;
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

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class PlaceGroupListDTO {
        private List<PlaceGroupDTO> placeGroupList;
        private Integer listSize;
    }
}
