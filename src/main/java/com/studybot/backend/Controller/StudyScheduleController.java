package com.studybot.backend.Controller;

import com.studybot.backend.DTOs.CreateScheduleDto;
import com.studybot.backend.DTOs.StudyBlockDto;
import com.studybot.backend.Entity.StudyBlock;
import com.studybot.backend.Entity.StudySchedule;
import com.studybot.backend.Entity.User;
import com.studybot.backend.Enum.DayEnum;
import com.studybot.backend.Repositories.StudyBlockRepository;
import com.studybot.backend.Repositories.StudyScheduleRepository;
import com.studybot.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class StudyScheduleController {

    private final StudyScheduleRepository scheduleRepo;
    private final StudyBlockRepository blockRepo;
    private final UserRepository userRepo;

    // ✅ CREATE SCHEDULE
    @PostMapping
    public StudySchedule createSchedule(@RequestBody CreateScheduleDto dto) {

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StudySchedule schedule = new StudySchedule();
        schedule.setUser(user);
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());

        if (dto.getActiveDays() != null) {
            schedule.setActiveDays(
                    dto.getActiveDays().stream()
                            .map(d -> DayEnum.valueOf(d.toUpperCase()))
                            .toList()
            );
        }

        return scheduleRepo.save(schedule);
    }

    // ✅ ADD BLOCK
    @PostMapping("/blocks")
    public StudyBlock addBlock(@RequestBody StudyBlockDto dto) {

        StudySchedule schedule = scheduleRepo.findById(dto.getScheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        StudyBlock block = new StudyBlock();
        block.setSchedule(schedule);
        block.setSubject(dto.getSubject());
        block.setStartTime(dto.getStartTime());
        block.setEndTime(dto.getEndTime());
        block.setOrderIndex(dto.getOrderIndex());

        schedule.getBlocks().add(block); // ✅ safe now

        return blockRepo.save(block);
    }

    // ✅ GET SCHEDULES BY USER
    @GetMapping("/user/{userId}")
    public List<StudySchedule> getSchedulesForUser(@PathVariable UUID userId) {
        return scheduleRepo.findByUser_Id(userId);
    }

    // ✅ GET BLOCKS
    @GetMapping("/{scheduleId}/blocks")
    public List<StudyBlock> getBlocksForSchedule(@PathVariable UUID scheduleId) {
        return blockRepo.findByScheduleId(scheduleId);
    }

    @DeleteMapping("/blocks/{blockId}")
    public void deleteBlock(@PathVariable UUID blockId) {
        blockRepo.deleteById(blockId);
    }

    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@PathVariable UUID scheduleId) {
        scheduleRepo.deleteById(scheduleId);
    }
}
