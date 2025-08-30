package com.example.feedbackbot.service;

import com.example.feedbackbot.config.GoogleDocsConfig;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.*;

import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class GoogleDocsService {

    private static final String APPLICATION_NAME = "Feedback Bot";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final Docs docsService;
    private final String documentId;

    public GoogleDocsService() throws Exception {
        this.documentId = GoogleDocsConfig.DOCUMENT_ID;
        this.docsService = initDocsService();
    }

    private Docs initDocsService() throws Exception {
        InputStream in = GoogleDocsService.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new RuntimeException("Не найден credentials.json (положи его в src/main/resources/)");
        }

        var httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new Docs.Builder(httpTransport, JSON_FACTORY, GoogleOAuth.getCredentials(httpTransport))
                .setApplicationName(APPLICATION_NAME)
                .build();
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
}

