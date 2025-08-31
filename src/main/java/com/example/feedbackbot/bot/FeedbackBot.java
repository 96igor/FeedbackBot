package com.example.feedbackbot.bot;

import com.example.feedbackbot.model.Feedback;
import com.example.feedbackbot.service.FeedbackService;
import com.example.feedbackbot.service.OpenAIService;
import com.example.feedbackbot.service.GoogleDocsService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.format.DateTimeFormatter;

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

            // Анализируем текст через OpenAI
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
            message.setText("Спасибо за ваш отзыв! Он был обработан как: " + feedback.getSentiment()
                    + " с критичностью " + feedback.getSeverity());
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String formatFeedbackForDocs(Feedback feedback) {
        String timestamp = feedback.getCreatedAt() != null ?
                feedback.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "неизвестно";
        return String.format("[%s] Роль: %s, Филиал: %s, Критичность: %d, Текст: %s",
                timestamp,
                feedback.getRole(),
                feedback.getBranch(),
                feedback.getSeverity(),
                feedback.getMessage());
    }


    @Override
    public String getBotUsername() {
        return "AutoServiceFeedbackBot";
    }

    @Override
    public String getBotToken() {
        return "8148238420:AAF0qhpT-03eW2NKxQ9wqcbRPINVx_xUOiY";
    }
}
