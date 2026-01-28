package com.example.pace.domain.member.dto.response;

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
        private String savedPlaceId;
        private String groupName;
        private String placeName;
        private String placeId;
        private String createdAt;
    }
}
