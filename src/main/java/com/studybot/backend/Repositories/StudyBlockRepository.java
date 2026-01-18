package com.studybot.backend.Repositories;

import com.studybot.backend.Entity.StudyBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudyBlockRepository extends JpaRepository<StudyBlock, UUID> {
    List<StudyBlock> findByScheduleId(UUID scheduleId);
}