package com.studybot.backend.Repositories;

import com.studybot.backend.Entity.SentReminder;
import com.studybot.backend.Enum.SubjectEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public interface SentReminderRepository extends JpaRepository<SentReminder, UUID> {

    boolean existsByUserIdAndSubjectAndDateAndTime(
            UUID userId,
            SubjectEnum subject,
            LocalDate date,
            LocalTime time
    );

    boolean existsByUserIdAndSubjectAndDate(
            UUID userId,
            SubjectEnum subject,
            LocalDate date
    );
}
