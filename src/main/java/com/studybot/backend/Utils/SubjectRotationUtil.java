package com.studybot.backend.Utils;

import com.studybot.backend.Enum.SubjectEnum;

import java.util.List;

public class SubjectRotationUtil {

    private static final List<SubjectEnum> subjects = List.of(
            SubjectEnum.MATH,
            SubjectEnum.PHYSICS,
            SubjectEnum.ENGLISH,
            SubjectEnum.CHEMISTRY,
            SubjectEnum.TECH
    );

    /**
     * Get subject for today based on day number
     */
    public static SubjectEnum getTodaySubject(int dayIndex) {
        return subjects.get(dayIndex % subjects.size());
    }
}
