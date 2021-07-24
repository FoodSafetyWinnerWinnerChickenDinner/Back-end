package com.example.backend.service;

import com.example.backend.configurations.OpenApiConfig;
import com.example.backend.configurations.RestTemplateConfig;
import com.example.backend.repositories.FoodRepository;
import com.example.backend.services.FoodOpenApi;
import com.example.backend.utils.Cast;
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

    @Mock private FoodRepository foodRepository;
    @Mock private OpenApiConfig foodApiConfig;
    @Mock private RestTemplateConfig restTemplate;
    @Mock private Cast cast;

    @InjectMocks
    private FoodOpenApi foodOpenApi;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
//        foodOpenApi = new FoodOpenApi(
//                foodApiConfig, foodRepository, restTemplate, cast
//        );
    }

    @Test
    @DisplayName("")
    public void already_contains_field(){
    }
}
