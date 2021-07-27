package com.example.backend.util_components;

import com.example.backend.util_components.interfaces.connecting.Connectable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.UnknownContentTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

            openApiUrl = urlBuilder.openApiUrlBuilder(key, name ,start ,end);

        }
        catch (UnsupportedEncodingException exception) {
            LOGGER.error(">>> OpenApi Connection >> exception >> ", exception);
        }

        Mono<String> webMono = openApiWebClient
                .get().uri(openApiUrl)
                .retrieve()
                .bodyToMono(String.class);

        return webMono.block();
    }

}
