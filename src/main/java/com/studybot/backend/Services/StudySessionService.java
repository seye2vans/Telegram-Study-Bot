package com.studybot.backend.Services;

import com.studybot.backend.Entity.StudySession;
import com.studybot.backend.Entity.User;
import com.studybot.backend.Enum.SessionStatus;
import com.studybot.backend.Enum.SubjectEnum;
import com.studybot.backend.Repositories.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository sessionRepo;

    public void createSession(User user, String subject) {
        LocalDate today = LocalDate.now();

        // ✅ Use User object in repository query
        List<StudySession> existing = sessionRepo.findByUserAndDateAndStatus(user, today, SessionStatus.PENDING);

        boolean exists = existing.stream().anyMatch(s -> s.getSubject().name().equals(subject));

        if (!exists) {
            StudySession session = new StudySession();
            session.setUser(user);
            session.setDate(today);

            // ✅ Correct Enum
            session.setSubject(SubjectEnum.valueOf(subject));
            session.setStatus(SessionStatus.PENDING);

            sessionRepo.save(session);
        }
    }

    public void markDone(StudySession session) {
        session.setStatus(SessionStatus.DONE);
        sessionRepo.save(session);
    }
}
