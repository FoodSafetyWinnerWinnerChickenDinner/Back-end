package com.example.backend.controllers;

import com.example.backend.services.FoodOpenApiService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FoodController {
    @Autowired
    FoodOpenApiService openApi;

    @GetMapping("/api/v1/foods") public ResponseEntity<JSONObject> get() {
        JSONObject response = openApi.requestFoods();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
