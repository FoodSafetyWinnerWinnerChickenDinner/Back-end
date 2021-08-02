package com.example.backend.controller;

import com.example.backend.controllers.FoodController;
import com.example.backend.services.service_foods.FoodServiceImpl;
import com.example.backend.services.service_nutrients.NutrientServiceImpl;
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

    private FoodServiceImpl foodServiceImpl;

    private NutrientServiceImpl nutrientServiceImpl;

    @Before
    void setup(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new FoodController(foodServiceImpl, nutrientServiceImpl)
        ).build();
    }

    @Test
    @DisplayName("")
    public void print() throws Exception {
    }
}
