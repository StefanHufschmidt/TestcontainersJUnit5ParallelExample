package com.example;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@FreshContainersPerTestClass
public class BooksProviderContainerPerClassTest {

    @BeforeAll
    public void setup(ContainerHolder containerHolder) {
        var mockServer = containerHolder.getWebserver();
        @Language("JSON")
        var booksResponse = """
                {
                    "books": [
                        {
                            "author": "Christian Ullenboom",
                            "title": "Java ist auch eine Insel"
                        },
                        {
                            "author": "Neal Ford, Mark Richards",
                            "title": "Fundamentals of Software Architecture"
                        }
                    ]
                }
                """;

        new MockServerClient(mockServer.getHost(), mockServer.getServerPort())
                .when(HttpRequest.request().withPath("/api/books"))
                    .respond(HttpResponse.response().withBody(booksResponse)
                            .withContentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldLoadBooks(ContainerHolder containerHolder) throws Exception {
        // given: instance with correct endpoint to use
        var booksProvider = new BooksProvider(containerHolder.getWebserver().getEndpoint());

        // when: requesting books
        final BooksResponse booksResponse = booksProvider.requestBooks();

        // then: response contains two books
        assertEquals(2, booksResponse.books().length);
    }

    @Test
    public void shouldLoadCorrectBooks(ContainerHolder containerHolder) throws Exception {
        // given: instance with correct endpoint to use
        var booksProvider = new BooksProvider(containerHolder.getWebserver().getEndpoint());

        // when: requesting books
        final BooksResponse booksResponse = booksProvider.requestBooks();

        // then: response contains Java book
        assertTrue(Arrays.stream(booksResponse.books()).anyMatch((book ->
                book.author().equals("Christian Ullenboom")
                        && book.title().equals("Java ist auch eine Insel")
                )));

        // and: response contains pattern book
        assertTrue(Arrays.stream(booksResponse.books()).anyMatch((book ->
                book.author().equals("Neal Ford, Mark Richards")
                        && book.title().equals("Fundamentals of Software Architecture")
                )));
    }

    @Test
    public void shouldLoadBooksTwice(ContainerHolder containerHolder) throws Exception {
        // given: instance with correct endpoint to use
        var booksProvider = new BooksProvider(containerHolder.getWebserver().getEndpoint());

        // when: requesting books
        final BooksResponse booksResponse = booksProvider.requestBooks();

        // then: response contains two books
        assertEquals(2, booksResponse.books().length);

        // when: requesting again
        final BooksResponse booksResponse2 = booksProvider.requestBooks();

        // then: response of second request contains two books as well
        assertEquals(2, booksResponse2.books().length);
    }
}
