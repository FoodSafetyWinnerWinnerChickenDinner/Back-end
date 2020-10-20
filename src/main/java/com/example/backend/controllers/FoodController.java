package com.example.backend.controllers;

import com.example.backend.models.Foods;
import com.example.backend.services.FoodServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin
@EnableScheduling
@RequestMapping("foods")
public class FoodController {
    private final FoodServiceImpl foodServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    public FoodController(FoodServiceImpl foodServiceImpl) {
        this.foodServiceImpl = foodServiceImpl;
    }

    @PostMapping("/recommend")
    public ResponseEntity<ArrayList<Foods>> menuRecommender(@RequestBody ArrayList<String> ate) {
        ArrayList<Foods> recommends = null;

        //test
        foodServiceImpl.foodListUpdater();
        foodServiceImpl.categorySetter();
        foodServiceImpl.exceptCategorySetter();

        try {
            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(ate);       // if null returns -> invalid category

            ArrayList<Foods>[] candidates = foodServiceImpl.extractCandidates(ingested);
            recommends = foodServiceImpl.menuRecommendation(candidates);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommends, HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void dataUpdateScheduler() {
        foodServiceImpl.dataUpdateProcessorByFoodOpenApi();         // all of three methods are always run here
        foodServiceImpl.foodListUpdater();
        foodServiceImpl.categorySetter();
        foodServiceImpl.exceptCategorySetter();
    }
}
