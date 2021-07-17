package com.example.backend.controllers;

import com.example.backend.models.Foods;
import com.example.backend.models.data_enums.Nutrients;
import com.example.backend.services.FoodServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodServiceImpl foodServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcTemplate.class);

    @PostMapping("/recommend")
    public ResponseEntity<List<Foods>> menuRecommender(@RequestBody ArrayList<String> ate) {
        List<Foods> recommends = null;

        List<Foods> foodList = foodServiceImpl.foodListExtractFromDB();
        HashMap<String, Nutrients> categories = foodServiceImpl.categorySetter();

        try {
            double[] ingested = foodServiceImpl.ingestedTotalNutrientsGetter(ate, categories);
            recommends = foodServiceImpl.menuRecommendation(ingested, foodList);
        }
        catch (NullPointerException nullPointerException) {
            LOGGER.error(">>> FoodController >> exception >> ", nullPointerException);
            nullPointerException.printStackTrace();
        }

        return new ResponseEntity(recommends, HttpStatus.OK);
    }
}
