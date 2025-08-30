package com.example.feedbackbot.service;

import com.example.feedbackbot.model.Feedback;
import com.example.feedbackbot.repository.FeedbackRepository;

import java.time.LocalDateTime;

public class FeedbackService {

    private final FeedbackRepository repository;
    private final OpenAIService openAIService;

    public FeedbackService(FeedbackRepository repository, OpenAIService openAIService) {
        this.repository = repository;
        this.openAIService = openAIService;
    }

    public Feedback processMessage(String message) {
        Feedback feedback = new Feedback();
        feedback.setRole("Механик"); // В будущем можно получать из состояния пользователя
        feedback.setBranch("Главный филиал");
        feedback.setMessage(message);
        feedback.setCreatedAt(LocalDateTime.now());

        // Используем OpenAI для анализа
        feedback.setSentiment(openAIService.detectSentiment(message));
        feedback.setSeverity(openAIService.detectSeverity(message));

        return feedback;
    }

    public void saveFeedback(Feedback feedback) {
        repository.save(feedback);
    }
}

