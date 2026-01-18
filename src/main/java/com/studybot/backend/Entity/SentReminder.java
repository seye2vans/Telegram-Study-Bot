package com.studybot.backend.Entity;

import com.studybot.backend.Enum.SubjectEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Table(
        name = "sent_reminder",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "subject", "date", "time"}
        )
)
public class SentReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubjectEnum subject;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;
}
