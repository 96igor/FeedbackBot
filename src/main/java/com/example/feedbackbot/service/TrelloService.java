package com.example.feedbackbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;

public class TrelloService {

    private final String apiKey;
    private final String apiToken;
    private final String boardId;
    private final String listId;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public TrelloService(String apiKey, String apiToken, String boardId, String listId) {
        this.apiKey = apiKey;
        this.apiToken = apiToken;
        this.boardId = boardId;
        this.listId = listId;
    }

    public void createCard(String name, String description) throws IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.trello.com")
                .addPathSegment("1")
                .addPathSegment("cards")
                .addQueryParameter("key", apiKey)
                .addQueryParameter("token", apiToken)
                .addQueryParameter("idList", listId)
                .addQueryParameter("name", name)
                .addQueryParameter("desc", description)
                .build();

        // пустое тело для POST-запроса
        RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to create Trello card: " + response.body().string());
            }
        }
    }
}
