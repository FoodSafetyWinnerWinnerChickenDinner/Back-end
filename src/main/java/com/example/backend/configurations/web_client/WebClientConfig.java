package com.example.backend.configurations.web_client;

import com.example.backend.configurations.OpenApiConfig;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final OpenApiConfig openApiConfig;

    @Bean
    public WebClient openApiWebClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(
                        configurator -> configurator
                                .defaultCodecs()
                                .maxInMemorySize(10 * 1024 * 1024)
                )
                .build();

        return WebClient.builder().baseUrl(openApiConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

}
