package com.example.feedbackbot.service;

import com.example.feedbackbot.model.Feedback;
import com.example.feedbackbot.repository.FeedbackRepository;

import java.time.LocalDateTime;

public class FeedbackService {

    private final FeedbackRepository repository;
    private final OpenAIService openAIService;
    private final GoogleDocsService googleDocsService;
    private final TrelloService trelloService;

    public FeedbackService(FeedbackRepository repository,
                           OpenAIService openAIService,
                           GoogleDocsService googleDocsService,
                           TrelloService trelloService) {
        this.repository = repository;
        this.openAIService = openAIService;
        this.googleDocsService = googleDocsService;
        this.trelloService = trelloService;
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
        // Сохраняем в базу
        repository.save(feedback);

        // Сохраняем в Google Docs
        try {
            googleDocsService.appendFeedback(feedback.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении в Google Docs: " + e.getMessage());
            e.printStackTrace();
        }

        // Если критический отзыв, создаём Trello-карту
        if (feedback.getSeverity() >= 4) {
            try {
                trelloService.createCard("Критический отзыв", feedback.getMessage());
            } catch (Exception e) {
                System.err.println("Ошибка при создании Trello-карты: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
