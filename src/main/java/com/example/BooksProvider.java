package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class BooksProvider {

    private final URI booksEndpoint;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    BooksProvider(String booksHost) throws URISyntaxException {
        booksEndpoint = new URI(booksHost + "/api/books");
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public BooksResponse requestBooks() throws IOException, InterruptedException {
        System.out.println("Requesting books from endpoint: " + booksEndpoint);
        Thread.sleep(2_000); // simulate long lasting request
        final HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(booksEndpoint)
                .build();
        final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Got response from books endpoint: " + booksEndpoint);
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), BooksResponse.class);
        }
        return null;
    }

}