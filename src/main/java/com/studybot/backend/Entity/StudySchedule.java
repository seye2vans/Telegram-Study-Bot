package com.studybot.backend.Entity;

import com.studybot.backend.Enum.DayEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class StudySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ REQUIRED (you are using these)
    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "study_schedule_active_days",
            joinColumns = @JoinColumn(name = "schedule_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "day")
    private List<DayEnum> activeDays = new ArrayList<>();

    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<StudyBlock> blocks = new ArrayList<>();
}
