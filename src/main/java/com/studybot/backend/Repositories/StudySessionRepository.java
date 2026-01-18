package com.studybot.backend.Repositories;

import com.studybot.backend.Entity.StudySession;
import com.studybot.backend.Entity.User;
import com.studybot.backend.Enum.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {
    List<StudySession> findByUserAndDateAndStatus(User user, LocalDate date, SessionStatus status);
}
