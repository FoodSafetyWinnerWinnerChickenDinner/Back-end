package com.example.backend.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class HttpConnectionPropertiesConfig {
    @Value("${httpconnpool.max-total}")
    private int maxTotal;

    @Value("${httpconnpool.max-per-route}")
    private int maxPerRoute;

    @Value("${httpconnpool.timeout}")
    private int timeOut;
}
