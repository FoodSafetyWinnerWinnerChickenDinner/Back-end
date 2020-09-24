package com.example.backend.controllers;

import com.example.backend.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class FoodController {
    @Autowired
    FoodService foodService;

//    @GetMapping("/api/v1/foods") public ResponseEntity<String> get() {
//        return new ResponseEntity<>("return", HttpStatus.OK);
//    }

    @Scheduled(cron = "0 0 4 ? * 0")
    public ResponseEntity<String> dataUpdateScheduler() {
        foodService.dataUpdateProcessorByFoodOpenApi();

        return new ResponseEntity<>("return", HttpStatus.OK);
    }
}
