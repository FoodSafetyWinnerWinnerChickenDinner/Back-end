package com.example.backend.util_components.util_connector;

import com.example.backend.util_components.interfaces.connecting.AsyncConnectable;
import com.example.backend.util_components.util_string.OpenApiUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class AsyncOpenApiConnectorByWebClient implements AsyncConnectable {

    private final OpenApiUrlBuilder urlBuilder;
    private final WebClient openApiWebClient;

    private final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    @Async
    public CompletableFuture<String> apiAsyncAccess(String key, String serviceName, int S, int E) {

        String openApiUrl = null;

        int size = E;
        int start = S;
        int end = E;

        while(start <= size) {
            end = Math.min(end, size);

            try {

                openApiUrl = urlBuilder.openApiUrlBuilder(key, serviceName, start, end);

            }
            catch (UnknownContentTypeException | UnsupportedEncodingException exception) {
                LOGGER.error(">>> Open API >> Async Access >> ", exception);
                break;
            }

            start += INTERVAL;
            end += INTERVAL;
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        String finalOpenApiUrl = openApiUrl;

        future.complete(
                openApiWebClient.get()
                        .uri(finalOpenApiUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        );

        return future;
    }
}
