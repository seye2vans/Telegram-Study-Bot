package com.studybot.backend.Services;

import com.studybot.backend.Entity.User;
import com.studybot.backend.Enum.SubjectEnum;
import com.studybot.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramService extends TelegramLongPollingBot {

    private final UserRepository userRepository;
    private final AiService aiService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();
            String username = update.getMessage().getFrom().getFirstName();

            System.out.println("ChatId: " + chatId);
            System.out.println("Message: " + messageText);

            // Handle /start command
            if (messageText.equalsIgnoreCase("/start")) {
                userRepository.findByTelegramChatId(chatId)
                        .orElseGet(() -> {
                            User user = new User();
                            user.setName(username);
                            user.setTelegramChatId(chatId);
                            return userRepository.save(user);
                        });
                sendText(chatId, "👋 Welcome! You’ll receive study reminders here.");
                return;
            }

            // Handle /status command: show basic info
            if (messageText.equalsIgnoreCase("/status")) {
                userRepository.findByTelegramChatId(chatId).ifPresentOrElse(user -> {
                    StringBuilder status = new StringBuilder("📊 Your study status:\n");
                    Map<SubjectEnum, Integer> streaks = user.getStreaks();
                    if (streaks == null || streaks.isEmpty()) {
                        status.append("No streaks yet. Start studying! 📚");
                    } else {
                        streaks.forEach((subject, streak) -> status.append(subject)
                                .append(": ").append(streak).append(" days\n"));
                    }
                    sendText(chatId, status.toString());
                }, () -> sendText(chatId, "⚠️ You are not registered. Send /start to register."));
                return;
            }

            // Handle /streak command: show detailed streak per subject
            if (messageText.equalsIgnoreCase("/streak")) {
                userRepository.findByTelegramChatId(chatId).ifPresentOrElse(user -> {
                    StringBuilder streakMsg = new StringBuilder("🔥 Your current streaks:\n");
                    Map<SubjectEnum, Integer> streaks = user.getStreaks();
                    if (streaks == null || streaks.isEmpty()) {
                        streakMsg.append("No active streaks. Keep up the studying! 💪");
                    } else {
                        for (SubjectEnum subject : SubjectEnum.values()) {
                            int s = streaks.getOrDefault(subject, 0);
                            streakMsg.append(subject).append(": ").append(s).append(" days\n");
                        }
                    }
                    sendText(chatId, streakMsg.toString());
                }, () -> sendText(chatId, "⚠️ You are not registered. Send /start to register."));
                return;
            }

            // Register new users automatically if they send any message
            boolean isNewUser = userRepository.findByTelegramChatId(chatId).isEmpty();
            if (isNewUser) {
                User user = new User();
                user.setName(username);
                user.setTelegramChatId(chatId);
                userRepository.save(user);
                sendText(chatId, "✅ You're registered! Study reminders will come here.");
            }
        }
    }

    public void sendReminder(User user, SubjectEnum subject) {
        if (user.getTelegramChatId() == null) return;

        String tip = aiService.getTip(subject);

        // ✅ Get current streak for this subject safely
        int streak = 0;
        if (user.getStreaks() != null) {
            streak = user.getStreaks().getOrDefault(subject, 0);
        }

        // Compose reminder message
        String messageText = "📚 Time to study " + subject + ", " + user.getName() +
                "\n\n💡 Tip: " + tip +
                "\n🔥 Current streak: " + streak + " day" + (streak == 1 ? "" : "s");

        sendText(user.getTelegramChatId(), messageText);
    }

    private void sendText(String chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
