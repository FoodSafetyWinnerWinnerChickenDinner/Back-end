package com.example.backend.controllers;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.services.FoodServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@CrossOrigin
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

        ArrayList<Foods> foodList = foodServiceImpl.foodListExtractFromDB();
        HashSet<String> except = foodServiceImpl.exceptCategorySetter();
        HashMap<String, Nutrients> categories = foodServiceImpl.categorySetter();

        try {
            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(ate, categories);       // if null returns -> invalid category

            ArrayList<Foods>[] candidates = foodServiceImpl.extractCandidates(ingested, foodList, except);
            recommends = foodServiceImpl.menuRecommendation(candidates);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommends, HttpStatus.OK);
    }
}
