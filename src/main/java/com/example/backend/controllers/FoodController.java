package com.example.backend.controllers;

import com.example.backend.services.FoodServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@EnableScheduling
public class FoodController {
    @Autowired
    FoodServiceImpl foodServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @GetMapping("/api/v1/foods") public ResponseEntity<ArrayList<String>> get() {
//        foodServiceImpl.foodListUpdater();
        foodServiceImpl.categorySetter();

        // test start
        ArrayList<String> tester = new ArrayList<>();
        tester.add("치킨 1");
        tester.add("돼지구이 1");
        tester.add("떡볶이 2");
        tester.add("소구이 3");
        tester.add("생선회 1");
        // test end

        try {
            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(tester);       // if null returns -> invalid category
            foodServiceImpl.menuRecommendation(ingested);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity("null", HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodServiceImpl.dataUpdateProcessorByFoodOpenApi();         // all of three methods are always run here
        foodServiceImpl.foodListUpdater();
        foodServiceImpl.categorySetter();
    }
}
