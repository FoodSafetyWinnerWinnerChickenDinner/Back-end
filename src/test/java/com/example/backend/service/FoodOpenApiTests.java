package com.example.backend.service;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.repositories.FoodOpenApiRepository;
import com.example.backend.services.FoodOpenApi;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FoodOpenApiTests {

    @Mock private FoodOpenApiRepository foodOpenApiRepository;
    @Mock private OpenApiConfig foodApiConfig;
    @Mock private RestTemplateConfig restTemplate;

    @InjectMocks
    private FoodOpenApi foodOpenApi;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
        foodOpenApi = new FoodOpenApi(
                foodApiConfig, foodOpenApiRepository, restTemplate
        );
    }

    @Test
    @DisplayName("")
    public void already_contains_field(){
    }
}
