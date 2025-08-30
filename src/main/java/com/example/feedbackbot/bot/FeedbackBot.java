package com.example.feedbackbot.bot;

import com.example.feedbackbot.model.Feedback;
import com.example.feedbackbot.service.FeedbackService;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class FeedbackBot extends TelegramLongPollingBot {

    private final FeedbackService feedbackService;

    public FeedbackBot(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String text = update.getMessage().getText();

            // Сохраняем фидбек
            Feedback feedback = feedbackService.processMessage(text);
            feedbackService.saveFeedback(feedback);

            // Ответ пользователю
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Спасибо за ваш отзыв! Он был обработан как: " + feedback.getSentiment());
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "FeedbackBot";
    }

    @Override
    public String getBotToken() {
        return "ВАШ_ТОКЕН";
    }
}
