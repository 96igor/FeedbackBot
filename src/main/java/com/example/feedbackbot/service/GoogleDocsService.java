package com.example.feedbackbot.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.BatchUpdateDocumentRequest;
import com.google.api.services.docs.v1.model.InsertTextRequest;
import com.google.api.services.docs.v1.model.Request;
import com.google.api.services.docs.v1.model.Location;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

public class GoogleDocsService {

    private static final String APPLICATION_NAME = "Feedback Bot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final Docs docsService;
    private final String documentId;

    public GoogleDocsService(String credentialsPath, String documentId) throws Exception {
        this.documentId = documentId;

        // Загружаем Service Account Credentials
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new FileInputStream(credentialsPath)
        ).createScoped(Collections.singletonList("https://www.googleapis.com/auth/documents"));

        this.docsService = new Docs.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new HttpCredentialsAdapter(credentials)
        ).setApplicationName(APPLICATION_NAME).build();
    }

    /**
     * Добавляет текст в начало Google Docs документа
     */
    public void appendFeedback(String feedbackText) throws Exception {
        Request insertTextRequest = new Request()
                .setInsertText(new InsertTextRequest()
                        .setText(feedbackText + "\n")
                        .setLocation(new Location().setIndex(1)));

        BatchUpdateDocumentRequest body = new BatchUpdateDocumentRequest()
                .setRequests(Collections.singletonList(insertTextRequest));

        docsService.documents().batchUpdate(documentId, body).execute();
    }

    /**
     * Сохраняет объект Feedback в Google Docs
     */
    public void saveFeedback(com.example.feedbackbot.model.Feedback feedback) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String text = String.format(
                "Роль: %s\nФилиал: %s\nВремя: %s\nСообщение: %s\nSentiment: %s\nSeverity: %d\n\n",
                feedback.getRole(),
                feedback.getBranch(),
                feedback.getCreatedAt().format(formatter),
                feedback.getMessage(),
                feedback.getSentiment(),
                feedback.getSeverity()
        );

        appendFeedback(text);
    }
}
