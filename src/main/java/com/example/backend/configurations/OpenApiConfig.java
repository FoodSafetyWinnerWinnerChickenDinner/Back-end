package com.example.backend.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:openapi.properties")
public class OpenApiConfig {
    @Value("${apiAuthKey1}")
    private String key1;

    @Value("${apiAuthKey2}")
    private String key2;

    @Value("${foodSafetyUrl}")
    private String url;

    @Value("${nutrientDB}")
    private String nutrientServiceName;

    @Value("${recipeDB}")
    private String recipeServiceName;
}
