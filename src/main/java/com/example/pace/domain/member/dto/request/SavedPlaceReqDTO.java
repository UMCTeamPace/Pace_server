package com.example.pace.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SavedPlaceReqDTO {
    @Getter
    @NoArgsConstructor
    public static class SavedPlaceDTO {
        @NotBlank(message = "장소명은 필수입니다.")
        private String placeName;

        @NotBlank(message = "장소id는 필수입니다.")
        private String placeId;

        @NotNull(message = "그룹 id는 필수입니다.")
        private Long groupId;
    }

//    @Getter
//    @NoArgsConstructor
//    public static class DeletedPlaceDTO {
//        private Long placeId;
//    }
}
