package com.studybot.backend.DTOs;

import lombok.Data;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class CreateScheduleDto {
    private UUID userId;
    private LocalTime startTime;
    private LocalTime endTime;

    // Must be a List of Strings, not a single String
    private List<String> activeDays;
}
