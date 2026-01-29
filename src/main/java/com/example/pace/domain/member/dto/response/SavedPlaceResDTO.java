package com.example.pace.domain.member.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SavedPlaceResDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceDTO {
        private Long savedPlaceId;
        private Long groupId;
        private String placeName;
        private String placeId;
        private String createdAt;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlaceListDTO {
        private List<PlaceDTO> placeDTOList;
        private Integer count;
    }

//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class Del
}
