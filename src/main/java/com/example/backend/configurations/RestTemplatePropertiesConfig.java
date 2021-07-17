package com.example.backend.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Getter
@PropertySource("classpath:connection.properties")
public class RestTemplatePropertiesConfig {
    @Value("${connpool.max-total}")
    private int maxTotal;

    @Value("${connpool.max-per-route}")
    private int maxPerRoute;

    @Value("${connpool.timeout}")
    private int timeOut;

    @Value("${connpool.keepalive}")
    private long keepAlive;
}
