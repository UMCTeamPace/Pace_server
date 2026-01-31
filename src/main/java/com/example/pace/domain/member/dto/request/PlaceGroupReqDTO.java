package com.example.pace.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PlaceGroupReqDTO {
    @Getter
    @NoArgsConstructor
    public static class SaveGroupReqDTO {
        @NotBlank(message = "그룹 이름은 필수입니다.")
        private String groupName;

        @NotBlank(message = "색상 코드 값은 필수입니다.")
        private String groupColor;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateGroupReqDTO {
        private String groupName;
        private String groupColor;
    }

    @Getter
    @NoArgsConstructor
    public static class DeleteGroupListReqDTO {
        @NotEmpty(message = "삭제할 그룹 ID 리스트는 필수입니다.")
        private List<Long> groupIdList;
    }
}
