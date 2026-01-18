package com.studybot.backend.Repositories;


import com.studybot.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByTelegramChatId(String telegramChatId);

}
