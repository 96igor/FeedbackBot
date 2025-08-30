package com.example.feedbackbot;

import com.example.feedbackbot.bot.FeedbackBot;
import com.example.feedbackbot.repository.FeedbackRepository;
import com.example.feedbackbot.service.FeedbackService;
import com.example.feedbackbot.service.OpenAIService;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) throws Exception {
        FeedbackRepository repository = new FeedbackRepository();
        OpenAIService openAIService = new OpenAIService();
        FeedbackService feedbackService = new FeedbackService(repository, openAIService);

        FeedbackBot bot = new FeedbackBot(feedbackService);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
        System.out.println("Bot started!");
    }
}
