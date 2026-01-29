package com.example.pace.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SavedPlaceReqDTO {
    @Getter
    @NoArgsConstructor
    public static class SavedPlaceDTO {
        @NotBlank(message = "장소 이름은 필수입니다.")
        private String placeName;

        @NotBlank(message = "장소 그룹명은 필수입니다.")
        private String groupName;

        @NotBlank(message = "장소id는 필수입니다.")
        private String placeId;
    }
}
