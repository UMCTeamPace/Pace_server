package com.example.pace.domain.schedule.controller;

import com.example.pace.domain.schedule.controller.ScheduleConversionControllerDocs;
import com.example.pace.domain.schedule.dto.response.ScheduleConversionResDto;
import com.example.pace.domain.schedule.service.ScheduleConversionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Schedule")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schedule")
public class ScheduleConversionController implements ScheduleConversionControllerDocs {

    private final ScheduleConversionService scheduleConversionService;

    @PatchMapping("/{id}/conversion")
    @Override
    public ScheduleConversionResDto convertPathScheduleToNormal(
            @PathVariable("id") Long scheduleId
    ) {
        return scheduleConversionService.convertPathScheduleToNormal(scheduleId);
    }
}
