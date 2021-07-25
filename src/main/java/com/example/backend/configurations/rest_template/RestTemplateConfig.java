package com.example.backend.configurations.rest_template;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final RestTemplatePropertiesConfig restTemplatePropertiesConfig;

    @Bean
    public RestTemplate getCustomRestTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(restTemplatePropertiesConfig.getTimeOut());

        HttpClient httpClient = HttpClientBuilder.create()
//                .setMaxConnTotal(restTemplatePropertiesConfig.getMaxTotal())
//                .setMaxConnPerRoute(restTemplatePropertiesConfig.getMaxPerRoute())
//                .setConnectionTimeToLive(restTemplatePropertiesConfig.getKeepAlive(), TimeUnit.SECONDS)           // keep-alive
                .build();

        httpRequestFactory.setHttpClient(httpClient);
        return new RestTemplate(httpRequestFactory);
    }

}
