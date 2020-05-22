package br.com.metys.kotlinspringgraphqlpoc;

import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class GraphQLDataFetchers {

    private static List<Map<String, String>> books = Arrays.asList(
            ImmutableMap.of("id", "book-1",
                    "name", "Harry Potter and the Philosopher's Stone",
                    "pageCount", "223",
                    "authorId", "author-1"),
            ImmutableMap.of("id", "book-2",
                    "name", "Moby Dick",
                    "pageCount", "635",
                    "authorId", "author-2"),
            ImmutableMap.of("id", "book-3",
                    "name", "Interview with the vampire",
                    "pageCount", "371",
                    "authorId", "author-3")
    );

    private static List<Map<String, String>> authors = Arrays.asList(
            ImmutableMap.of("id", "author-1",
                    "firstName", "Joanne",
                    "lastName", "Rowling"),
            ImmutableMap.of("id", "author-2",
                    "firstName", "Herman",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "author-3",
                    "firstName", "Anne",
                    "lastName", "Rice")
    );

    private static List<Map<String, String>> persons = Arrays.asList(
            ImmutableMap.of("id", "person-1",
                    "firstName", "Bruno",
                    "lastName", "Barros"),
            ImmutableMap.of("id", "person-2",
                    "firstName", "Henrique",
                    "lastName", "Melville"),
            ImmutableMap.of("id", "person-3",
                    "firstName", "Renan",
                    "lastName", "Rice")
    );

    public DataFetcher getBookByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String bookId = dataFetchingEnvironment.getArgument("id");
            return books
                    .stream()
                    .filter(book -> book.get("id").equals(bookId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getAuthorDataFetcher() {
        return dataFetchingEnvironment -> {
            Map<String, String> book = dataFetchingEnvironment.getSource();
            String authorId = book.get("authorId");
            return authors
                    .stream()
                    .filter(author -> author.get("id").equals(authorId))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getPersonByIdDataFetcher() {
        return dataFetchingEnvironment -> {
            String personId = dataFetchingEnvironment.getArgument("id");
            return persons
                    .stream()
                    .filter(book -> book.get("id").equals(personId))
                    .findFirst()
                    .orElse(null);
        };
    }
    public DataFetcher getPersonByIdAndFirstNameDataFetcher() {
        return dataFetchingEnvironment -> {
            String personId = dataFetchingEnvironment.getArgument("id");
            String personFirstName = dataFetchingEnvironment.getArgument("firstName");
            return persons
                    .stream()
                    .filter(book -> book.get("id").equals(personId) && book.get("firstName").equals(personFirstName))
                    .findFirst()
                    .orElse(null);
        };
    }
}
