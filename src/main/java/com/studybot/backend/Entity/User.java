package com.studybot.backend.Entity;

import com.studybot.backend.Enum.SubjectEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Entity
@Data
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    private String whatsappNumber;

    private String timezone = "Africa/Lagos"; // default

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "streak")
    private Map<SubjectEnum, Integer> streaks = new HashMap<>();
}
