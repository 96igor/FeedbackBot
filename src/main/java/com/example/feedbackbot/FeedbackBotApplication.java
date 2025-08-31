package com.example.feedbackbot;

import com.example.feedbackbot.bot.FeedbackBot;
import com.example.feedbackbot.repository.FeedbackRepository;
import com.example.feedbackbot.service.FeedbackService;
import com.example.feedbackbot.service.GoogleDocsService;
import com.example.feedbackbot.service.OpenAIService;
import com.example.feedbackbot.service.TrelloService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class FeedbackBotApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Репозиторий для PostgreSQL
        FeedbackRepository repository = new FeedbackRepository();

        // OpenAI
        OpenAIService openAIService = new OpenAIService(
                "sk-your-openai-key" // ключ теперь можно брать из application.properties
        );

        // Google Docs
        GoogleDocsService googleDocsService = new GoogleDocsService(
                "src/main/resources/credentials/credentials.json",
                "1ri0hQwtsZK2mI78hS_i3VTGm4w3RW0IF5oRYCTPLp7s"
        );

        // Trello
        TrelloService trelloService = new TrelloService(
                "your-trello-api-key",
                "your-trello-api-token",
                "your-board-id",
                "your-list-id"
        );

        // Feedback Service
        FeedbackService feedbackService = new FeedbackService(
                repository,
                openAIService,
                googleDocsService,
                trelloService
        );

        // Телеграм бот
        FeedbackBot bot = new FeedbackBot(feedbackService, openAIService, googleDocsService);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        System.out.println("Bot started!");
    }
}
