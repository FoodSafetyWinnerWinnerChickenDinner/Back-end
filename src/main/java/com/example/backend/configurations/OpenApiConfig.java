package com.example.backend.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@PropertySource("classpath:openapi.properties")
public class OpenApiConfig {
    @Value("${apiAuthKey}")
    private String key;

    @Value("${foodSafetyUrl}")
    private String url;
}
