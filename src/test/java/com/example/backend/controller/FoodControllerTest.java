package com.example.backend.controller;

import com.example.backend.controllers.FoodController;
import com.example.backend.services.FoodService;
import com.example.backend.services.NutrientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class FoodControllerTest {

    private MockMvc mockMvc;

    private FoodService foodService;

    private NutrientService nutrientService;

    @Before
    void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new FoodController(foodService, nutrientService)
        ).build();
    }

    @Test
    @DisplayName("")
    public void print() throws Exception {
    }
}
