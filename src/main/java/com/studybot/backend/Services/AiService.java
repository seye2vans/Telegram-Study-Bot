package com.studybot.backend.Services;

import com.studybot.backend.Enum.SubjectEnum;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    public String getTip(SubjectEnum subject) {

        return switch (subject) {
            case MATH -> "Practice problems instead of rereading notes.";
            case ENGLISH -> "Read aloud to improve comprehension.";
            case PHYSICS -> "Focus on understanding formulas, not memorizing.";
            case CHEMISTRY -> "Write reactions from memory to test yourself.";
            case TECH -> "Stay focused and study in short, consistent sessions.";
        };
    }
}
