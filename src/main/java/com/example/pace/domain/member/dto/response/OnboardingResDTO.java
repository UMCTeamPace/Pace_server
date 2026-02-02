package com.example.pace.domain.member.dto.response;

import com.example.pace.domain.member.enums.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record OnboardingResDTO(
        boolean onboardingCompleted,
        String accessToken,
        String refreshToken,
        Integer earlyArrivalTime,
        Boolean isReminderActive,
        Role role
) {
}
