package com.example.feedbackbot.service;

import com.example.feedbackbot.model.Feedback;

import java.time.LocalDateTime;
import java.util.List;

public class OpenAIService {

    private final String apiKey; // можно использовать потом для реального вызова OpenAI

    public OpenAIService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Анализирует текст и возвращает sentiment: negative / neutral / positive
     */
    public String detectSentiment(String message) {
        if (message.contains("плохо") || message.contains("задержка")) return "negative";
        else if (message.contains("хорошо") || message.contains("спасибо")) return "positive";
        return "neutral";
    }

    public int detectSeverity(String message) {
        if (message.contains("плохо") || message.contains("задержка")) return 4;
        return 2;
    }

    /**
     * Создаёт объект Feedback с анализом
     */
    public Feedback analyzeFeedback(String message) {
        Feedback feedback = new Feedback();
        feedback.setMessage(message);
        feedback.setRole("Механик"); // можно потом брать из состояния пользователя
        feedback.setBranch("Главный филиал");
        feedback.setSentiment(detectSentiment(message));
        feedback.setSeverity(detectSeverity(message));
        feedback.setCreatedAt(LocalDateTime.now());
        return feedback;
    }

    // Здесь можно добавить реальный вызов OpenAI Chat API позже
    public String analyzeWithOpenAI(String message) {
        // пример для будущего использования с gpt-3.5-turbo или gpt-4
        // ChatCompletionRequest request = ChatCompletionRequest.builder()
        //      .model("gpt-3.5-turbo")
        //      .messages(List.of(new ChatMessage("user", message)))
        //      .build();
        // return openAiService.createChatCompletion(request)
        //      .getChoices().get(0).getMessage().getContent();
        return "neutral"; // заглушка
    }
}
