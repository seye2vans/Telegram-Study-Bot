package com.studybot.backend.Entity;

import com.studybot.backend.Enum.SessionStatus;
import com.studybot.backend.Enum.SubjectEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class StudySession {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id") // explicitly set FK column
    private User user;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private SubjectEnum subject;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;
}
