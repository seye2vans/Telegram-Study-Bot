package com.studybot.backend.Schedular;

import com.studybot.backend.Entity.SentReminder;
import com.studybot.backend.Entity.StudyBlock;
import com.studybot.backend.Entity.StudySchedule;
import com.studybot.backend.Enum.DayEnum;
import com.studybot.backend.Enum.SubjectEnum;
import com.studybot.backend.Repositories.SentReminderRepository;
import com.studybot.backend.Repositories.StudyScheduleRepository;
import com.studybot.backend.Repositories.UserRepository;
import com.studybot.backend.Services.TelegramService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final StudyScheduleRepository scheduleRepo;
    private final SentReminderRepository sentReminderRepo;
    private final UserRepository userRepo;
    private final TelegramService telegramService;

    // ⏰ Runs every minute for reminders
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sendReminders() {

        for (StudySchedule schedule : scheduleRepo.findAll()) {

            // Use user timezone or default
            String timezone = schedule.getUser().getTimezone() != null
                    ? schedule.getUser().getTimezone()
                    : ZoneId.systemDefault().toString();

            ZonedDateTime userNow = ZonedDateTime.now(ZoneId.of(timezone));
            LocalDate today = userNow.toLocalDate();
            LocalTime now = userNow.toLocalTime().truncatedTo(ChronoUnit.MINUTES);
            DayEnum todayEnum = DayEnum.valueOf(userNow.getDayOfWeek().name().substring(0, 3));

            // Skip schedules not active today
            if (!schedule.getActiveDays().contains(todayEnum)) continue;

            for (StudyBlock block : schedule.getBlocks()) {

                // 🔥 Trigger ONLY at block start
                if (!now.equals(block.getStartTime())) continue;

                // Prevent duplicates
                boolean alreadySent = sentReminderRepo.existsByUserIdAndSubjectAndDateAndTime(
                        schedule.getUser().getId(),
                        block.getSubject(),
                        today,
                        now
                );

                if (alreadySent) {
                    log.debug("⏭ Duplicate prevented for {}", block.getSubject());
                    continue;
                }

                try {
                    // ✅ Send reminder
                    telegramService.sendReminder(schedule.getUser(), block.getSubject());

                    // ✅ Record sent reminder
                    SentReminder sent = new SentReminder();
                    sent.setUser(schedule.getUser());
                    sent.setSubject(block.getSubject());
                    sent.setDate(today);
                    sent.setTime(now);
                    sentReminderRepo.save(sent);

                    // ✅ Update streak safely
                    if (schedule.getUser().getStreaks() == null) {
                        schedule.getUser().setStreaks(new HashMap<>());
                    }
                    Map<SubjectEnum, Integer> streaks = schedule.getUser().getStreaks();
                    streaks.put(block.getSubject(), streaks.getOrDefault(block.getSubject(), 0) + 1);
                    userRepo.save(schedule.getUser());

                    log.info("✅ Reminder sent for {} | Current streak: {}",
                            block.getSubject(),
                            streaks.get(block.getSubject())
                    );

                } catch (Exception e) {
                    log.error("❌ Failed to send reminder for {} to user {}",
                            block.getSubject(),
                            schedule.getUser().getName(), e);
                }
            }
        }
    }

    // 🕛 Reset missed streaks daily per user timezone
    @Scheduled(cron = "0 0 0 * * *") // system midnight
    @Transactional
    public void resetMissedStreaks() {
        for (var user : userRepo.findAll()) {

            if (user.getStreaks() == null) user.setStreaks(new HashMap<>());

            String timezone = user.getTimezone() != null
                    ? user.getTimezone()
                    : ZoneId.systemDefault().toString();

            LocalDate yesterday = ZonedDateTime.now(ZoneId.of(timezone))
                    .minusDays(1).toLocalDate();

            for (SubjectEnum subject : SubjectEnum.values()) {
                boolean studiedYesterday = sentReminderRepo.existsByUserIdAndSubjectAndDate(
                        user.getId(), subject, yesterday
                );

                if (!studiedYesterday) {
                    user.getStreaks().put(subject, 0); // reset streak
                    log.info("🔄 Streak reset for user {} on subject {}", user.getName(), subject);
                }
            }

            userRepo.save(user);
        }
    }
}
