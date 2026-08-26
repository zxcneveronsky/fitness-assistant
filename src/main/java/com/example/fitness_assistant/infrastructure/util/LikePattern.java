package com.example.fitness_assistant.infrastructure.util;

public final class LikePattern {

    private LikePattern() {
    }

    public static String escape(String value) {
        if (value == null) {
            return null;
        }
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
