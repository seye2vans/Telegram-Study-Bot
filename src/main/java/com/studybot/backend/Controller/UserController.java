package com.studybot.backend.Controller;

import com.studybot.backend.Entity.User;
import com.studybot.backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepo;

    @PostMapping
    public User create(@RequestBody User user) {
        return userRepo.save(user);
    }

    @GetMapping
    public List<User> all() {
        return userRepo.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id) {
        return userRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
