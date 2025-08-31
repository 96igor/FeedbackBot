package com.example.feedbackbot.bot;

import com.example.feedbackbot.model.Feedback;
import com.example.feedbackbot.service.FeedbackService;
import com.example.feedbackbot.service.OpenAIService;
import com.example.feedbackbot.service.GoogleDocsService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FeedbackBot extends TelegramLongPollingBot {

    private final FeedbackService feedbackService;
    private final OpenAIService openAIService;
    private final GoogleDocsService googleDocsService;

    public FeedbackBot(FeedbackService feedbackService, OpenAIService openAIService, GoogleDocsService googleDocsService) {
        this.feedbackService = feedbackService;
        this.openAIService = openAIService;
        this.googleDocsService = googleDocsService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            // Создаём Feedback через OpenAIService
            Feedback feedback = openAIService.analyzeFeedback(text);

            // Сохраняем в БД
            feedbackService.saveFeedback(feedback);

            // Отправляем в Google Docs
            try {
                googleDocsService.appendFeedback(formatFeedbackForDocs(feedback));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Ответ пользователю
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Спасибо за отзыв! Sentiment: " + feedback.getSentiment()
                    + ", Severity: " + feedback.getSeverity());
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatFeedbackForDocs(Feedback feedback) {
        return String.format("Роль: %s, Филиал: %s, Severity: %d, Sentiment: %s, Текст: %s",
                feedback.getRole(),
                feedback.getBranch(),
                feedback.getSeverity(),
                feedback.getSentiment(),
                feedback.getMessage());
    }

    @Override
    public String getBotUsername() {
        return "AutoServiceFeedbackBot";
    }

    @Override
    public String getBotToken() {
        return "8148238420:AAF0qhpT-03eW2NKxQ9wqcbRPINVx_xUOiY"; // замени на свой токен
    }
}
