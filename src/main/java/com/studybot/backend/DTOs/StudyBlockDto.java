package com.studybot.backend.DTOs;
import com.studybot.backend.Enum.SubjectEnum;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Data
public class StudyBlockDto {
    private UUID scheduleId;
    private SubjectEnum subject;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer orderIndex;
}