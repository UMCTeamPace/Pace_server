package com.example.pace.domain.member.converter;

import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.enums.AlarmType;

import java.util.*;
import java.util.stream.Collectors;

public class OnboardingConverter {
    private OnboardingConverter() {}

    public static Map<AlarmType, List<Integer>> toAlarmMap(List<OnboardingReqDTO.AlarmConfig> alarms) {
        if (alarms == null) return Collections.emptyMap();

        Map<AlarmType, List<Integer>> map = new EnumMap<>(AlarmType.class);

        for (OnboardingReqDTO.AlarmConfig cfg : alarms) {
            AlarmType type = cfg.type();
            List<Integer> minutes = cfg.minutes();

            if (minutes == null) {
                map.put(type, List.of());
                continue;
            }

            List<Integer> normalized = minutes.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .sorted(Comparator.reverseOrder()) // 큰 값 먼저(원하면 naturalOrder)
                    .collect(Collectors.toList());

            map.put(type, normalized);
        }

        return map;
    }
}
