package com.example.feedbackbot;

import com.example.feedbackbot.bot.FeedbackBot;
import com.example.feedbackbot.repository.FeedbackRepository;
import com.example.feedbackbot.service.FeedbackService;
import com.example.feedbackbot.service.GoogleDocsService;
import com.example.feedbackbot.service.OpenAIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class FeedbackBotApplication implements CommandLineRunner {

    @Value("${openai.api.key}")
    private String openAIKey;

    @Value("${google.credentials.file}")
    private String googleCredentialsFile;

    @Value("${google.document.id}")
    private String googleDocumentId;

    public static void main(String[] args) {
        SpringApplication.run(FeedbackBotApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Репозиторий для PostgreSQL
        FeedbackRepository repository = new FeedbackRepository();

        // OpenAI
        OpenAIService openAIService = new OpenAIService(openAIKey);

        // Сервис работы с Feedback
        FeedbackService feedbackService = new FeedbackService(repository, openAIService);

        // Google Docs
        GoogleDocsService googleDocsService = new GoogleDocsService(
                googleCredentialsFile,
                googleDocumentId
        );

        // Телеграм бот
        FeedbackBot bot = new FeedbackBot(feedbackService, openAIService, googleDocsService);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);

        System.out.println("Bot started!");
    }
}
