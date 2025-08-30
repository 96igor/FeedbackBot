package com.example.feedbackbot.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.InputStream;
import java.util.Collections;

public class GoogleOAuth {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Получение Credentials для Google Docs API
     */
    public static GoogleCredential getCredentials(HttpTransport httpTransport) throws Exception {
        InputStream in = GoogleOAuth.class.getResourceAsStream("/credentials/credentials.json");
        if (in == null) {
            throw new RuntimeException("Не найден файл credentials.json в src/main/resources/credentials/");
        }

        return GoogleCredential.fromStream(in, httpTransport, JSON_FACTORY)
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/documents"));
    }
}
