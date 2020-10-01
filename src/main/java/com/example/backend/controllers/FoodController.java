package com.example.backend.controllers;

import com.example.backend.api_config.NutrientsConfigs;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.services.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class FoodController {
    @Autowired
    FoodServiceImpl foodServiceImpl;

    @GetMapping("/api/v1/foods") public ResponseEntity<String> get() {
        foodServiceImpl.foodListUpdater();          // test
        return new ResponseEntity<>("null", HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodServiceImpl.dataUpdateProcessorByFoodOpenApi();
        foodServiceImpl.foodListUpdater();
    }
}
