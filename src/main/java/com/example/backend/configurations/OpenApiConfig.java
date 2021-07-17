package com.example.backend.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:openapi.properties")
public class OpenApiConfig {
    @Value("${apiAuthKey}")
    private String key;

    @Value("${foodSafetyUrl}")
    private String url;

    @Value("${nutrientDB}")
    private String nutrientServiceName;

    @Value("${recipeDB}")
    private String recipeServiceName;
}
