package com.example.pace.domain.member.service;

import com.example.pace.domain.member.converter.OnboardingConverter;
import com.example.pace.domain.member.dto.request.OnboardingReqDTO;
import com.example.pace.domain.member.dto.response.OnboardingResDTO;
import com.example.pace.domain.member.entity.Member;
import com.example.pace.domain.member.entity.Setting;
import com.example.pace.domain.member.enums.AlarmType;
import com.example.pace.domain.member.enums.CalendarType;
import com.example.pace.domain.member.exception.OnboardingErrorCode;
import com.example.pace.domain.member.exception.OnboardingException;
import com.example.pace.domain.member.repository.MemberRepository;
import com.example.pace.domain.member.repository.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final MemberRepository memberRepository;
    private final SettingRepository settingRepository;

    @Transactional
    public OnboardingResDTO upsertOnboarding(Long memberId, OnboardingReqDTO request) {

        // 0) member 검증
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new OnboardingException(OnboardingErrorCode.MEMBER_NOT_FOUND));

        // 1) Setting upsert (member 1:1)
        Setting setting = settingRepository.findByMember(member)
                .orElseGet(() -> settingRepository.save(Setting.defaultOf(member)));

        // 2) setting 기본 값 업데이트 (온보딩에서 받는 값)
        // OnboardingReqDTO가 record면 request.xxx() / 일반 DTO면 request.getXxx()
        setting.update(
                request.earlyArrivalTime(),   // Integer earlyArrivalTime
                null,                         // isNotiEnabled: 온보딩에서 안 받으면 유지
                null,                         // isLocEnabled: 온보딩에서 안 받으면 유지
                request.isReminderActive(),   // Boolean isReminderActive
                request.calendarType()     // CalendarType
        );

        // 3) 알림 시간 갱신 (타입별 교체)
        Map<AlarmType, List<Integer>> alarmMap = OnboardingConverter.toAlarmMap(request.alarms());

        // 정책: 요청에 들어온 타입만 교체 (안 들어온 타입은 유지)
        for (Map.Entry<AlarmType, List<Integer>> entry : alarmMap.entrySet()) {
            setting.replaceReminderTimes(entry.getKey(), entry.getValue());
        }

        // 4) 온보딩 완료 처리 (Member에 컬럼 있으면 true 세팅)
        // member.updateOnboardingCompleted(true);

        return new OnboardingResDTO(true);
    }
}
