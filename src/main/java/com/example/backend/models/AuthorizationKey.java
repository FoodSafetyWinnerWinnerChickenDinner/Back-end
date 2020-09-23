package com.example.backend.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
public class AuthorizationKey {
    @Value("${apiAuthKey}")
    private String key;
}
