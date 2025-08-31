package com.example.feedbackbot.service;

import com.example.feedbackbot.model.Feedback;

import java.time.LocalDateTime;

public class OpenAIService {

    public String detectSentiment(String message) {
        if (message.contains("плохо") || message.contains("задержка")) return "negative";
        else if (message.contains("хорошо") || message.contains("спасибо")) return "positive";
        return "neutral";
    }

    public int detectSeverity(String message) {
        if (message.contains("плохо") || message.contains("задержка")) return 4;
        return 2;
    }

    // Новый метод для бота
    public Feedback analyzeFeedback(String message) {
        Feedback feedback = new Feedback();
        feedback.setMessage(message);
        feedback.setSentiment(detectSentiment(message));
        feedback.setSeverity(detectSeverity(message));
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setRole("Механик");       // Заглушка
        feedback.setBranch("Главный филиал"); // Заглушка
        return feedback;
    }
}


