package com.studybot.backend.Controller;

import com.studybot.backend.Enum.SubjectEnum;
import com.studybot.backend.Entity.User;
import com.studybot.backend.Repositories.UserRepository;
import com.studybot.backend.Services.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TelegramService telegramService;
    private final UserRepository userRepository;

    @GetMapping("/test-telegram")
    public String testTelegram() {

        User user = userRepository.findById(
                UUID.fromString("3db682cd-67d1-486f-b2d7-19b98bd9d15d")
        ).orElseThrow(() -> new RuntimeException("User not found"));

        telegramService.sendReminder(user, SubjectEnum.MATH);

        return "Telegram reminder sent!";
    }
}
