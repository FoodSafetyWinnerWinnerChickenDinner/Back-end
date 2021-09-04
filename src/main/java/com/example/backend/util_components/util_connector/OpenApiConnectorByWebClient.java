package com.example.backend.util_components.util_connector;

import com.example.backend.util_components.interfaces.connecting.Connectable;
import com.example.backend.util_components.util_string.OpenApiUrlBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class OpenApiConnectorByWebClient implements Connectable {

    private final OpenApiUrlBuilder urlBuilder;

    private final WebClient openApiWebClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public String requestOpenApiData(String key, String name, int start, int end) throws UnknownContentTypeException {

        String openApiUrl = null;

        try {

            openApiUrl = urlBuilder.openApiUrlBuilder(key, name, start, end);

        }
        catch (UnsupportedEncodingException exception) {
            LOGGER.error(">>> OpenApi Connection >> exception >> ", exception);
        }

        String finalOpenApiUrl = openApiUrl;

        return openApiWebClient
                .get()
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
                .bodyToMono(String.class)
                .block();

    }
}
