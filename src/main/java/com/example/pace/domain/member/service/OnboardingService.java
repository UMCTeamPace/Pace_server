package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.SettingConverter;
import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.dto.response.OnboardingResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.ReminderTime;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.enums.Role;
import com.example.pace.domain.member.exception.OnboardingErrorCode;
import com.example.pace.domain.member.exception.OnboardingException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SettingRepository;
import com.example.pace.global.auth.JwtUtil;
import java.util.EnumSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private static final int MAX_ALARMS_PER_TYPE = 5;
    private static final int MAX_EARLY_ARRIVAL_MINUTES = 60;
    private static final Set<AlarmType> REQUIRED_TYPES = EnumSet.of(AlarmType.SCHEDULE, AlarmType.DEPARTURE);

    //ui 선택지 기반 whitelist
    private static final Set<Integer> ALLOWED_SCHEDULE_MINUTES = Set.of(
            5, 10, 15, 30, 60, 120,
            1440,   // 1일
            2880,   // 2일
            10080   // 1주
    );
    private static final Set<Integer> ALLOWED_DEPARTURE_MINUTES = Set.of(
            5, 10, 15, 20, 25, 30, 35, 45, 50, 55, 60
    );

    private final MemberRepository memberRepository;
    private final SettingServiceImpl settingService;
    private final JwtUtil jwtUtil;

    @Transactional
    public OnboardingResDTO upsertOnboarding(Long memberId, OnboardingReqDTO request) {
        // member 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OnboardingException(OnboardingErrorCode.MEMBER_NOT_FOUND));

        // Setting upsert (member 1:1)
        Setting setting = settingService.getOrCreateSetting(member);

        // earlyArrivalTime 검증 (0~60)
        Integer earlyArrivalTime = request.earlyArrivalTime();
        if (earlyArrivalTime == null || earlyArrivalTime < 0 || earlyArrivalTime > MAX_EARLY_ARRIVAL_MINUTES) {
            throw new OnboardingException(OnboardingErrorCode.INVALID_EARLY_ARRIVAL_TIME);
        }

        // alarms 정규화:  항상 SCHEDULE/DEPARTURE 둘 다 포함시키기 (없으면 빈 리스트로)
        Map<AlarmType, List<Integer>> alarmMap = toValidatedAlarmMap(request.alarms());

        // setting 기본 값 업데이트 (온보딩에서 받는 값)
        setting.update(
                earlyArrivalTime,   // Integer earlyArrivalTime
                null,                         // isNotiEnabled: 온보딩에서 안 받으면 유지
                null,                         // isLocEnabled: 온보딩에서 안 받으면 유지
                request.isReminderActive(),
                request.calendarType()
        );

        // 알림 시간 갱신 (타입별 교체)(빈 리스트면 해당 타입 전부 삭제)
        for (AlarmType type : REQUIRED_TYPES) {
            List<Integer> minutes = alarmMap.getOrDefault(type, Collections.emptyList());
            settingService.replaceReminderTimesFromMinutes(setting, type, minutes);
        }

        //온보딩 완료 처리
        member.updateOnboardingCompleted();

        String accessToken = jwtUtil.createAccessToken(member.getId(), Role.ROLE_USER);
        String refreshToken = jwtUtil.createRefreshToken(member.getId());
        member.updateRefreshToken(refreshToken);

        return OnboardingResDTO.builder()
                .onboardingCompleted(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .earlyArrivalTime(setting.getEarlyArrivalTime())
                .isReminderActive(setting.getIsReminderActive())
                .role(Role.ROLE_USER)
                .build();
    }

    /**
     * 타입별: - distinct 후에도 size<=5 - 값 범위/화이트리스트 검증(옵션) - null minutes는 빈 리스트로
     */
    private Map<AlarmType, List<Integer>> toValidatedAlarmMap(List<OnboardingReqDTO.AlarmConfig> alarms) {
        Map<AlarmType, List<Integer>> out = new EnumMap<>(AlarmType.class);

        // 1) 우선 request 값 모으기 (혹시 중복 type이 와도 합쳐서 distinct 처리)
        if (alarms != null) {
            for (OnboardingReqDTO.AlarmConfig alarm : alarms) {
                if (alarm == null || alarm.type() == null) {
                    continue;
                }

                AlarmType type = alarm.type();
                List<Integer> minutes = alarm.minutes() == null ? Collections.emptyList() : alarm.minutes();

                out.computeIfAbsent(type, k -> new ArrayList<>()).addAll(minutes);
            }
        }

        // 2) REQUIRED_TYPES 보정 + distinct + 최대 5개 검증
        for (AlarmType type : REQUIRED_TYPES) {
            List<Integer> raw = out.getOrDefault(type, Collections.emptyList());

            List<Integer> distinct = raw.stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(Collectors.toList());

            if (distinct.size() > MAX_ALARMS_PER_TYPE) {
                throw new OnboardingException(OnboardingErrorCode.TOO_MANY_ALARMS);
            }

            validateAllowedMinutes(type, distinct);

            out.put(type, distinct);
        }

        return out;
    }

    private void validateAllowedMinutes(AlarmType type, List<Integer> minutes) {
        Set<Integer> allowed = (type == AlarmType.SCHEDULE)
                ? ALLOWED_SCHEDULE_MINUTES
                : ALLOWED_DEPARTURE_MINUTES;

        for (Integer m : minutes) {
            if (!allowed.contains(m)) {
                throw new OnboardingException(OnboardingErrorCode.INVALID_ALARM_MINUTES);
            }
        }
    }

}
