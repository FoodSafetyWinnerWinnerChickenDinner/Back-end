package com.example.backend.controllers;

import com.example.backend.services.FoodOpenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController {
    @Autowired
    FoodOpenApiService openApi;

    @GetMapping("/api/v1/foods") public String get() {
        return openApi.requestFoods();
    }
}
