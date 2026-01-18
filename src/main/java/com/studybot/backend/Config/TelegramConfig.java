package com.studybot.backend.Config;

import com.studybot.backend.Services.TelegramService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@RequiredArgsConstructor
public class TelegramConfig {

    private final TelegramService telegramService;

    @PostConstruct
    public void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramService);
            System.out.println("✅ Telegram bot registered successfully");
        } catch (Exception e) {
            System.err.println("❌ Failed to register Telegram bot");
            e.printStackTrace();
        }
    }
}
