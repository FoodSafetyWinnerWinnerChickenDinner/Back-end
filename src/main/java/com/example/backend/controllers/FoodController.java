package com.example.backend.controllers;

import com.example.backend.services.FoodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableScheduling
public class FoodController {
    @Autowired
    FoodServiceImpl foodServiceImpl;

//    @GetMapping("/api/v1/foods") public ResponseEntity<String> get() {
//        return new ResponseEntity<>("return", HttpStatus.OK);
//    }

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodServiceImpl.dataUpdateProcessorByFoodOpenApi();
    }
}
