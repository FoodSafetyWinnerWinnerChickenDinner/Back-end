package com.example.backend.controllers;

import com.example.backend.models.Foods;
import com.example.backend.services.FoodServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@EnableScheduling
public class FoodController {
    @Autowired
    FoodServiceImpl foodServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @PostMapping("/api/v1/foods")
    public ResponseEntity<ArrayList<Foods>> menuRecommender(@RequestBody ArrayList<String> ate) {
        // test start
        foodServiceImpl.categorySetter();
        foodServiceImpl.exceptCategorySetter();

        ArrayList<String> tester = new ArrayList<>();           // input parameter
        tester.add("라면 2");
        // test end

        try {
//            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(ate);
            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(tester);       // if null returns -> invalid category
            foodServiceImpl.foodListUpdater();                                              // test
            ArrayList<Foods>[] candidates = foodServiceImpl.extractCandidates(ingested);

            return new ResponseEntity(foodServiceImpl.menuRecommendation(candidates), HttpStatus.OK);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity("null", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodServiceImpl.dataUpdateProcessorByFoodOpenApi();         // all of three methods are always run here
        foodServiceImpl.foodListUpdater();
        foodServiceImpl.categorySetter();
        foodServiceImpl.exceptCategorySetter();
    }
}
