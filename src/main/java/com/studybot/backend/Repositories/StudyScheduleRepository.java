package com.studybot.backend.Repositories;

import com.studybot.backend.Entity.StudySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudyScheduleRepository
        extends JpaRepository<StudySchedule, UUID> {

    List<StudySchedule> findByUser_Id(UUID userId);
}
