package com.example.pace.domain.member.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OnboardingResDTO(
        @JsonProperty("onboarding_completed")
        boolean onboardingCompleted
) {}
