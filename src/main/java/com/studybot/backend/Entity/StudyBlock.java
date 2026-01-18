package com.studybot.backend.Entity;

import com.studybot.backend.Enum.SubjectEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
public class StudyBlock {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "schedule_id") // explicitly set FK column
    private StudySchedule schedule;

    @Enumerated(EnumType.STRING)
    private SubjectEnum subject;

    private LocalTime startTime;
    private LocalTime endTime;
    private int orderIndex;
}
