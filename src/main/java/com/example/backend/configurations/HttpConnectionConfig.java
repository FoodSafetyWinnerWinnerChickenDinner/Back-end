package com.example.backend.configurations;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpConnectionConfig {
    private final HttpConnectionPropertiesConfig httpConnectionPropertiesConfig;

    public HttpConnectionConfig(HttpConnectionPropertiesConfig httpConnectionPropertiesConfig) {
        this.httpConnectionPropertiesConfig = httpConnectionPropertiesConfig;
    }

    @Bean
    public RestTemplate getCustomRestTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(httpConnectionPropertiesConfig.getTimeOut());

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(httpConnectionPropertiesConfig.getMaxTotal())
                .setMaxConnPerRoute(httpConnectionPropertiesConfig.getMaxPerRoute())
                .build();

        httpRequestFactory.setHttpClient(httpClient);
        return new RestTemplate(httpRequestFactory);
    }

}
