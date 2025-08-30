package com.example.feedbackbot.service;

public class OpenAIService {

    public String detectSentiment(String message) {
        // Заглушка, позже можно подключить OpenAI API
        if (message.contains("плохо") || message.contains("задержка")) return "negative";
        else if (message.contains("хорошо") || message.contains("спасибо")) return "positive";
        return "neutral";
    }

    public int detectSeverity(String message) {
        if (message.contains("плохо") || message.contains("задержка")) return 4;
        return 2;
    }
}

