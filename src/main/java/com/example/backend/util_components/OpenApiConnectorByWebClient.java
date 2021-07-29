package com.example.backend.util_components;

import com.example.backend.util_components.interfaces.connecting.Connectable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class OpenApiConnectorByWebClient implements Connectable {

    private final OpenApiUrlBuilder urlBuilder;

    private final WebClient openApiWebClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
//    @Async
    public String requestOpenApiData(String key, String name, int start, int end) throws UnknownContentTypeException, ExecutionException, InterruptedException {

        String openApiUrl = null;

        try {

            openApiUrl = urlBuilder.openApiUrlBuilder(key, name, start, end);

        }
        catch (UnsupportedEncodingException exception) {
            LOGGER.error(">>> OpenApi Connection >> exception >> ", exception);
        }

        String finalOpenApiUrl = openApiUrl;

//        CompletableFuture<String> future = new CompletableFuture<>();
//        future.complete(
//                openApiWebClient.get()
//                .uri(finalOpenApiUrl)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block()
//        );

        return openApiWebClient.get()
                .uri(finalOpenApiUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public String responseTotalIndex(String key, String name, int start, int end) {

        String openApiUrl = null;

        try {

            openApiUrl = urlBuilder.openApiUrlBuilder(key, name, start, end);

        } catch (UnsupportedEncodingException exception) {
            LOGGER.error(">>> OpenApi Connection >> exception >> ", exception);
        }

        return openApiWebClient.get()
                .uri(openApiUrl)
                .retrieve()
                .bodyToMono(String.class).block();
    }
}
